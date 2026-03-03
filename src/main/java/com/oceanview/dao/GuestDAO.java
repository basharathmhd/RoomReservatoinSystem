package com.oceanview.dao;

import com.oceanview.modal.Guest;

import java.sql.*;
import java.util.List;

public class GuestDAO extends BaseDAO<Guest> {

    private static final String TABLE_NAME = "guests";

    private static final String INSERT_SQL = "INSERT INTO " + TABLE_NAME
            + " (guest_id, first_name, last_name, address, " +
            "contact_number, email, identification_number, date_of_birth, nationality, " +
            "created_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE " + TABLE_NAME
            + " SET first_name = ?, last_name = ?, address = ?, " +
            "contact_number = ?, email = ?, identification_number = ?, date_of_birth = ?, " +
            "nationality = ?, modified_date = ?, modified_by = ? WHERE guest_id = ?";

    private static final String DELETE_SQL = "DELETE FROM " + TABLE_NAME + " WHERE guest_id = ?";

    private static final String SELECT_BY_ID_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE guest_id = ?";

    private static final String SELECT_ALL_SQL = "SELECT * FROM " + TABLE_NAME;

    private final RowMapper<Guest> guestMapper = rs -> {
        Guest guest = new Guest();
        guest.setGuestId(rs.getString("guest_id"));
        guest.setFirstName(rs.getString("first_name"));
        guest.setLastName(rs.getString("last_name"));
        guest.setAddress(rs.getString("address"));
        guest.setContactNumber(rs.getString("contact_number"));
        guest.setEmail(rs.getString("email"));
        guest.setIdentificationNumber(rs.getString("identification_number"));

        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            guest.setDateOfBirth(dob.toLocalDate());
        }

        guest.setNationality(rs.getString("nationality"));

        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            guest.setCreatedDate(createdDate.toLocalDateTime());
        }

        guest.setCreatedBy(rs.getString("created_by"));

        Timestamp modifiedDate = rs.getTimestamp("modified_date");
        if (modifiedDate != null) {
            guest.setModifiedDate(modifiedDate.toLocalDateTime());
        }

        guest.setModifiedBy(rs.getString("modified_by"));

        return guest;
    };

    @Override
    public Guest findById(String id) throws SQLException {
        return executeQuerySingle(SELECT_BY_ID_SQL, guestMapper, id);
    }

    @Override
    public List<Guest> findAll() throws SQLException {
        return executeQuery(SELECT_ALL_SQL, guestMapper);
    }

    @Override
    public boolean insert(Guest guest) throws SQLException {
        int result = executeUpdate(INSERT_SQL,
                guest.getGuestId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getAddress(),
                guest.getContactNumber(),
                guest.getEmail(),
                guest.getIdentificationNumber(),
                guest.getDateOfBirth() != null ? Date.valueOf(guest.getDateOfBirth()) : null,
                guest.getNationality(),
                Timestamp.valueOf(guest.getCreatedDate()),
                guest.getCreatedBy());
        return result > 0;
    }

    @Override
    public boolean update(Guest guest) throws SQLException {
        int result = executeUpdate(UPDATE_SQL,
                guest.getFirstName(),
                guest.getLastName(),
                guest.getAddress(),
                guest.getContactNumber(),
                guest.getEmail(),
                guest.getIdentificationNumber(),
                guest.getDateOfBirth() != null ? Date.valueOf(guest.getDateOfBirth()) : null,
                guest.getNationality(),
                guest.getModifiedDate() != null ? Timestamp.valueOf(guest.getModifiedDate()) : null,
                guest.getModifiedBy(),
                guest.getGuestId());
        return result > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        int result = executeUpdate(DELETE_SQL, id);
        return result > 0;
    }

    public List<Guest> searchByName(String name) throws SQLException {
        String sql = SELECT_ALL_SQL + " WHERE CONCAT(first_name, ' ', last_name) LIKE ?";
        return executeQuery(sql, guestMapper, "%" + name + "%");
    }
}

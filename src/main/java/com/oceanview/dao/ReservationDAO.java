package com.oceanview.dao;

import com.oceanview.modal.Reservation;
import com.oceanview.modal.Guest;
import com.oceanview.modal.Room;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class ReservationDAO extends BaseDAO<Reservation> {

    private static final String INSERT_SQL = "INSERT INTO reservations (reservation_number, guest_id, room_id, check_in_date, "
            +
            "check_out_date, number_of_guests, status, special_requests, created_by) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL = "SELECT r.*, g.first_name, g.last_name, g.contact_number, " +
            "rm.room_number, rm.floor FROM reservations r " +
            "LEFT JOIN guests g ON r.guest_id = g.guest_id " +
            "LEFT JOIN rooms rm ON r.room_id = rm.room_id " +
            "WHERE r.reservation_number = ?";

    private final RowMapper<Reservation> mapper = rs -> {
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(rs.getString("reservation_number"));
        reservation.setGuestId(rs.getString("guest_id"));
        reservation.setRoomId(rs.getString("room_id"));

        Date checkIn = rs.getDate("check_in_date");
        if (checkIn != null)
            reservation.setCheckInDate(checkIn.toLocalDate());

        Date checkOut = rs.getDate("check_out_date");
        if (checkOut != null)
            reservation.setCheckOutDate(checkOut.toLocalDate());

        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setStatus(rs.getString("status"));
        reservation.setSpecialRequests(rs.getString("special_requests"));

        return reservation;
    };

    @Override
    public Reservation findById(String id) throws SQLException {
        return executeQuerySingle(SELECT_BY_ID_SQL, mapper, id);
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        return executeQuery("SELECT * FROM reservations", mapper);
    }

    @Override
    public boolean insert(Reservation entity) throws SQLException {
        return executeUpdate(INSERT_SQL,
                entity.getReservationNumber(),
                entity.getGuestId(),
                entity.getRoomId(),
                Date.valueOf(entity.getCheckInDate()),
                Date.valueOf(entity.getCheckOutDate()),
                entity.getNumberOfGuests(),
                entity.getStatus(),
                entity.getSpecialRequests(),
                entity.getCreatedBy()) > 0;
    }

    @Override
    public boolean update(Reservation entity) throws SQLException {
        String sql = "UPDATE reservations SET guest_id=?, room_id=?, check_in_date=?, " +
                "check_out_date=?, number_of_guests=?, status=?, special_requests=? " +
                "WHERE reservation_number=?";
        return executeUpdate(sql,
                entity.getGuestId(),
                entity.getRoomId(),
                Date.valueOf(entity.getCheckInDate()),
                Date.valueOf(entity.getCheckOutDate()),
                entity.getNumberOfGuests(),
                entity.getStatus(),
                entity.getSpecialRequests(),
                entity.getReservationNumber()) > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return executeUpdate("DELETE FROM reservations WHERE reservation_number = ?", id) > 0;
    }

    public boolean checkAvailability(String roomId, LocalDate checkIn, LocalDate checkOut)
            throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE room_id = ? AND status IN ('CONFIRMED', 'CHECKED_IN') " +
                "AND ((check_in_date <= ? AND check_out_date > ?) " +
                "OR (check_in_date < ? AND check_out_date >= ?) " +
                "OR (check_in_date >= ? AND check_out_date <= ?))";
        return count(sql, roomId, checkIn, checkIn, checkOut, checkOut, checkIn, checkOut) == 0;
    }
}

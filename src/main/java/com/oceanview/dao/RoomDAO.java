package com.oceanview.dao;

import com.oceanview.modal.Room;

import java.sql.*;
import java.util.List;

public class RoomDAO extends BaseDAO<Room> {

    private static final String TABLE_NAME = "rooms";

    private static final String INSERT_SQL = "INSERT INTO " + TABLE_NAME
            + " (room_id, room_number, type_id, floor, capacity, status, amenities) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE " + TABLE_NAME + " SET room_number = ?, type_id = ?, floor = ?, " +
            "capacity = ?, status = ?, amenities = ? WHERE room_id = ?";

    private static final String DELETE_SQL = "DELETE FROM " + TABLE_NAME + " WHERE room_id = ?";

    private static final String SELECT_BY_ID_SQL = "SELECT r.*, rt.type_name, rt.base_rate, rt.max_occupancy FROM "
            + TABLE_NAME +
            " r LEFT JOIN room_types rt ON r.type_id = rt.type_id WHERE r.room_id = ?";

    private static final String SELECT_ALL_SQL = "SELECT r.*, rt.type_name, rt.base_rate, rt.max_occupancy FROM "
            + TABLE_NAME +
            " r LEFT JOIN room_types rt ON r.type_id = rt.type_id";

    private final RowMapper<Room> roomMapper = rs -> {
        Room room = new Room();
        room.setRoomId(rs.getString("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setTypeId(rs.getString("type_id"));
        room.setFloor(rs.getInt("floor"));
        room.setCapacity(rs.getInt("capacity"));
        room.setStatus(rs.getString("status"));
        room.setAmenities(rs.getString("amenities"));
        return room;
    };

    @Override
    public Room findById(String id) throws SQLException {
        return executeQuerySingle(SELECT_BY_ID_SQL, roomMapper, id);
    }

    @Override
    public List<Room> findAll() throws SQLException {
        return executeQuery(SELECT_ALL_SQL, roomMapper);
    }

    @Override
    public boolean insert(Room room) throws SQLException {
        int result = executeUpdate(INSERT_SQL,
                room.getRoomId(),
                room.getRoomNumber(),
                room.getTypeId(),
                room.getFloor(),
                room.getCapacity(),
                room.getStatus(),
                room.getAmenities());
        return result > 0;
    }

    @Override
    public boolean update(Room room) throws SQLException {
        int result = executeUpdate(UPDATE_SQL,
                room.getRoomNumber(),
                room.getTypeId(),
                room.getFloor(),
                room.getCapacity(),
                room.getStatus(),
                room.getAmenities(),
                room.getRoomId());
        return result > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        int result = executeUpdate(DELETE_SQL, id);
        return result > 0;
    }

    public List<Room> findAvailableRooms() throws SQLException {
        String sql = SELECT_ALL_SQL + " WHERE r.status = 'AVAILABLE'";
        return executeQuery(sql, roomMapper);
    }
}

package com.oceanview.dao;

import com.oceanview.modal.RoomType;
import java.sql.SQLException;
import java.util.List;

public class RoomTypeDAO extends BaseDAO<RoomType> {

    private final RowMapper<RoomType> mapper = rs -> {
        RoomType rt = new RoomType();
        rt.setTypeId(rs.getString("type_id"));
        rt.setTypeName(rs.getString("type_name"));
        rt.setBaseRate(rs.getDouble("base_rate"));
        rt.setDescription(rs.getString("description"));
        rt.setMaxOccupancy(rs.getInt("max_occupancy"));
        rt.setAmenities(rs.getString("amenities"));
        return rt;
    };

    @Override
    public RoomType findById(String id) throws SQLException {
        String sql = "SELECT * FROM room_types WHERE type_id = ?";
        return executeQuerySingle(sql, mapper, id);
    }

    @Override
    public List<RoomType> findAll() throws SQLException {
        String sql = "SELECT * FROM room_types";
        return executeQuery(sql, mapper);
    }

    @Override
    public boolean insert(RoomType rt) throws SQLException {
        String sql = "INSERT INTO room_types (type_id, type_name, base_rate, description, max_occupancy, amenities) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, rt.getTypeId(), rt.getTypeName(), rt.getBaseRate(), rt.getDescription(),
                rt.getMaxOccupancy(), rt.getAmenities()) > 0;
    }

    @Override
    public boolean update(RoomType rt) throws SQLException {
        String sql = "UPDATE room_types SET type_name = ?, base_rate = ?, description = ?, max_occupancy = ?, amenities = ? WHERE type_id = ?";
        return executeUpdate(sql, rt.getTypeName(), rt.getBaseRate(), rt.getDescription(), rt.getMaxOccupancy(),
                rt.getAmenities(), rt.getTypeId()) > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM room_types WHERE type_id = ?";
        return executeUpdate(sql, id) > 0;
    }
}

package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public int getNextAvailableSlot(ParkingType parkingType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    public boolean updateParking(ParkingSpot parkingSpot) {
        // mettre à jour la disponibilité de cette place de stationnement

        Connection con = null;
        PreparedStatement ps = null;
        int updateRowCount = 0;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            updateRowCount = ps.executeUpdate();

        } catch (RuntimeException | SQLException | ClassNotFoundException ex) {
            logger.error("Error updating parking info", ex);
        }

        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
        dataBaseConfig.closeConnection(con);
        dataBaseConfig.closePreparedStatement(ps);
        return (updateRowCount > 0);
    }

    // creation de la methode pour ParkingDataBaseIT pour les tests d'integrations
    public ParkingSpot getParkingByParkingNumber(int i) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParkingSpot parkingspot = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT_BY_NUMBER);
            ps.setInt(1, i);
            rs = ps.executeQuery();
            if (rs.next()) {
                ParkingType pt;
                if (rs.getString(3).equals("CAR"))
                    pt = ParkingType.CAR;
                else
                    pt = ParkingType.BIKE;
                parkingspot = new ParkingSpot(rs.getInt(1), pt, rs.getBoolean(2));
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closePreparedStatement(ps);
        }
        return parkingspot;
    }

}

package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ParkingSpotDAOTest {

    @Test
    public void testGetNextAvailableSlot() throws Exception {
        // Mocking dependencies
        DataBaseConfig dataBaseConfig = Mockito.mock(DataBaseConfig.class);
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseConfig;

        int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(1, nextAvailableSlot);
    }

    @Test
    public void testUpdateParking() throws Exception {
        // Mocking dependencies
        DataBaseConfig dataBaseConfig = Mockito.mock(DataBaseConfig.class);
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseConfig;

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpot.setId(1);
        parkingSpot.setAvailable(true);

        boolean isParkingUpdated = parkingSpotDAO.updateParking(parkingSpot);

        assertEquals(true, isParkingUpdated);
    }
}

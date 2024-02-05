package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TicketDAOTest {

    @Test
    public void testSaveTicket() throws Exception {
        DataBaseConfig dataBaseConfig = Mockito.mock(DataBaseConfig.class);
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        TicketDAO ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;

        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(10.0);
        ticket.setInTime(new Timestamp(System.currentTimeMillis()));

        boolean isTicketSaved = ticketDAO.saveTicket(ticket);

        assertEquals(false, isTicketSaved);
    }

    @Test
    public void testGetTicket() throws Exception {
        DataBaseConfig dataBaseConfig = Mockito.mock(DataBaseConfig.class);
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(6)).thenReturn("CAR");
        when(resultSet.getDouble(3)).thenReturn(10.0);
        when(resultSet.getTimestamp(4)).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(resultSet.getTimestamp(5)).thenReturn(null);

        TicketDAO ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;

        Ticket ticket = ticketDAO.getTicket("ABC123");

        assertEquals(1, ticket.getParkingSpot().getId());
        assertEquals("CAR", ticket.getParkingSpot().getParkingType().toString());
        assertEquals(10.0, ticket.getPrice());
    }

    @Test
    public void testUpdateTicket() throws Exception {
        DataBaseConfig dataBaseConfig = Mockito.mock(DataBaseConfig.class);
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        TicketDAO ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setPrice(15.0);
        ticket.setOutTime(new Timestamp(System.currentTimeMillis()));

        boolean isTicketUpdated = ticketDAO.updateTicket(ticket);

        assertEquals(true, isTicketUpdated);
    }

    @Test
    public void testGetNbTicket() throws Exception {
        DataBaseConfig dataBaseConfig = Mockito.mock(DataBaseConfig.class);
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        TicketDAO ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;

        int nbTicket = ticketDAO.getNbTicket("ABC123");

        assertEquals(3, nbTicket);
    }
}
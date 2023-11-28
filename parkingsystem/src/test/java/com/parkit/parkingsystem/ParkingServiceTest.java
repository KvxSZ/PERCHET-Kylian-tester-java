package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ParkingServiceTest {

    @InjectMocks
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void testProcessIncomingCar() throws Exception {
        String vehicleRegNumber = "ABC123";
        ParkingSpot mockParkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Date currentTime = new Date();

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        parkingService.processIncomingVehicle();

        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any());
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void testProcessIncomingBike() throws Exception {
        String vehicleRegNumber = "ABC123";
        ParkingSpot mockParkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
        Date currentTime = new Date();

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        parkingService.processIncomingVehicle();

        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any());
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingCarTest() throws Exception {
        String vehicleRegNumber = "ABC123";
        Ticket mockTicket = new Ticket();
        mockTicket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        mockTicket.setOutTime(new Date());
        mockTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(mockTicket);
        when(ticketDAO.getNbTicket(vehicleRegNumber)).thenReturn(1);
        when(ticketDAO.updateTicket(mockTicket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingBikeTest() throws Exception {
        String vehicleRegNumber = "ABC123";
        Ticket mockTicket = new Ticket();
        mockTicket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        mockTicket.setOutTime(new Date());
        mockTicket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, true));
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(mockTicket);
        when(ticketDAO.getNbTicket(vehicleRegNumber)).thenReturn(1);
        when(ticketDAO.updateTicket(mockTicket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }


    @Test
    public void processExitingVehicleTestUnableUpdate() throws Exception {

        String vehicleRegNumber = "ABC123";
        Ticket mockTicket = new Ticket();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(mockTicket);
        when(ticketDAO.getNbTicket(vehicleRegNumber)).thenReturn(2);
        when(ticketDAO.updateTicket(mockTicket)).thenReturn(false);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailable() {
        ParkingSpot mockParkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        ParkingSpot resultSpot = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertEquals(mockParkingSpot.getId(), resultSpot.getId());
        Assertions.assertEquals(mockParkingSpot.getParkingType(), resultSpot.getParkingType());
        Assertions.assertTrue(resultSpot.isAvailable());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(-1);

        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertNull(parkingSpot);
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
        when(inputReaderUtil.readSelection()).thenReturn(3);
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(-1);

        ParkingSpot resultSpot = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertNull(resultSpot);
        verify(inputReaderUtil, times(1)).readSelection();
    }

    @Test
    public void testParkingLotExitRecurringUser() throws Exception {
        String vehicleRegNumber = "ABC123";
        Ticket mockTicket = new Ticket();
        mockTicket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        mockTicket.setOutTime(new Date());
        mockTicket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, true));
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(mockTicket);
        when(ticketDAO.getNbTicket(vehicleRegNumber)).thenReturn(1);
        when(ticketDAO.updateTicket(mockTicket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processIncomingVehicle();

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));

    }

}

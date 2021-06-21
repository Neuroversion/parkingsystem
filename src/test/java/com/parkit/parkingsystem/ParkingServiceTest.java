package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static com.parkit.parkingsystem.constants.ParkingType.BIKE;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
    public class ParkingServiceTest {

    @Mock
    private static ParkingService parkingService;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private FareCalculatorService fareCalculatorService;

    public ParkingServiceTest() {

    }

    @BeforeEach
    public void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);
    }

    @Test
    public void processIncomingCarTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(CAR)).thenReturn(1);

        parkingService.processIncomingVehicle();

        ArgumentCaptor<ParkingSpot> parkingSpotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotDAO).updateParking(parkingSpotCaptor.capture());
        ParkingSpot updatedParkingSpot = parkingSpotCaptor.getValue();
        assertThat(updatedParkingSpot.isAvailable()).isFalse();
        assertThat(updatedParkingSpot.getParkingType()).isEqualTo(CAR);
        assertThat(updatedParkingSpot.getId()).isEqualTo(1);

        ArgumentCaptor<Ticket> saveTicketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketDAO).saveTicket(saveTicketCaptor.capture());
        Ticket saveTicket = saveTicketCaptor.getValue();
        assertThat(saveTicket.getInTime()).isNotNull();
        assertThat(saveTicket.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(saveTicket.getParkingSpot().getId()).isEqualTo(1);
        assertThat(saveTicket.getPrice()).isEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNull();

    }

    @Test
    public void processIncomingBikeTest() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

        parkingService.processIncomingVehicle();

        ArgumentCaptor<ParkingSpot> parkingSpotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotDAO).updateParking(parkingSpotCaptor.capture());
        ParkingSpot updatedParkingSpot = parkingSpotCaptor.getValue();
        assertThat(updatedParkingSpot.isAvailable()).isFalse();
        assertThat(updatedParkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
        assertThat(updatedParkingSpot.getId()).isEqualTo(4);

        ArgumentCaptor<Ticket> saveTicketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketDAO).saveTicket(saveTicketCaptor.capture());
        Ticket saveTicket = saveTicketCaptor.getValue();
        assertThat(saveTicket.getInTime()).isNotNull();
        assertThat(saveTicket.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(saveTicket.getParkingSpot().getId()).isEqualTo(4);
        assertThat(saveTicket.getPrice()).isEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNull();

    }

    @Test
    public void processExitingCarTest() {
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(new Date(new Date().getTime() - 3600000));
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
        when(ticketDAO.isUsualVehicle("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(ticket.getParkingSpot())).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(fareCalculatorService).calculateFare(ticket,true);

        ArgumentCaptor<ParkingSpot> parkingSpotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotDAO).updateParking(parkingSpotCaptor.capture());
        ParkingSpot updatedParkingSpot = parkingSpotCaptor.getValue();
        assertThat(updatedParkingSpot.isAvailable()).isTrue();

        ArgumentCaptor<Ticket> saveTicketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketDAO).updateTicket(saveTicketCaptor.capture());
        Ticket saveTicket = saveTicketCaptor.getValue();
        assertThat(saveTicket.getInTime()).isNotNull();
        assertThat(saveTicket.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(saveTicket.getPrice()).isGreaterThanOrEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNotNull().isCloseTo(new Date(), 1000);

    }

    @Test
    public void processExitingBikeTest() {
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(3, BIKE, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(new Date(new Date().getTime() - 3600000));
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
        when(ticketDAO.isUsualVehicle("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(ticket.getParkingSpot())).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(fareCalculatorService).calculateFare(ticket,true);

        ArgumentCaptor<ParkingSpot> parkingSpotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotDAO).updateParking(parkingSpotCaptor.capture());
        ParkingSpot updatedParkingSpot = parkingSpotCaptor.getValue();
        assertThat(updatedParkingSpot.isAvailable()).isTrue();

        ArgumentCaptor<Ticket> saveTicketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketDAO).updateTicket(saveTicketCaptor.capture());
        Ticket saveTicket = saveTicketCaptor.getValue();
        assertThat(saveTicket.getInTime()).isNotNull();
        assertThat(saveTicket.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(saveTicket.getPrice()).isGreaterThanOrEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNotNull().isCloseTo(new Date(), 1000);

        }

    }




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
    //@Mock
    //private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private FareCalculatorService fareCalculatorService;


    public ParkingServiceTest(Object xNumber) {

    }

    @BeforeEach
    public void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);
    }

    @Test
    public void processIncomingCarTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        parkingService.processIncomingVehicle();

        ArgumentCaptor<ParkingSpot> parkingSpotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotDAO).updateParking(parkingSpotCaptor.capture());
        ParkingSpot updatedParkingSpot = parkingSpotCaptor.getValue();
        assertThat(updatedParkingSpot.isAvailable()).isFalse();
        assertThat(updatedParkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(updatedParkingSpot.getId()).isEqualTo(1);

        ArgumentCaptor<Ticket> saveTicketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketDAO).saveTicket(saveTicketCaptor.capture());
        Ticket saveTicket = saveTicketCaptor.getValue();
        assertThat(saveTicket.getInTime()).isNotNull();
        assertThat(saveTicket.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(saveTicket.getParkingSpot(true).getId()).isEqualTo(1);
        assertThat(saveTicket.getPrice()).isEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNull();

    }
    // faire la meme chose pour une moto, une sortie moto et voiture.

    @Test
    public void processIncomingBikeTest() throws Exception {
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
        assertThat(saveTicket.getParkingSpot(true).getId()).isEqualTo(4);
        assertThat(saveTicket.getPrice()).isEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNull();

    }


    @Test
    public void processExitingCarTest() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        //ParkingSpot parkingSpot = ticket.getParkingSpot(true);
        //parkingSpot.setAvailable(true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
        when(ticketDAO.isUsualVehicle("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(ticket.getParkingSpot(true))).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(fareCalculatorService).calculateFare(ticket,true);
        verify(parkingSpotDAO).updateParking(ticket.getParkingSpot(true));
    }

    @Test
    public void processExitingBikeTest() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = ticket.getParkingSpot(true);
        parkingSpot.setAvailable(true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
        when(ticketDAO.isUsualVehicle("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(ticket.getParkingSpot(true))).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(fareCalculatorService).calculateFare(ticket,true);
        verify(parkingSpotDAO).updateParking(ticket.getParkingSpot(true));
    }


}



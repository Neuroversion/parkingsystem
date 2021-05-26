package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
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

import static com.parkit.parkingsystem.service.ParkingService.fareCalculatorService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeEach
    private void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        dataBasePrepareService.clearDataBaseEntries();
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
        assertThat(saveTicket.getParkingSpot().getId()).isEqualTo(1);
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
        assertThat(saveTicket.getParkingSpot().getId()).isEqualTo(4);
        assertThat(saveTicket.getPrice()).isEqualTo(0.0);
        assertThat(saveTicket.getOutTime()).isNull();

    }
@Test
    public void processExitingCarTest() throws Exception {
    parkingService.processIncomingVehicle();
    Date inTime = new Date();
    inTime.setTime( inTime.getTime() - (  60 * 60 * 1000) );
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket,false);
    assertEquals(ticket.getPrice(), 0.5* Fare.CAR_RATE_PER_HOUR);
            String vehicleRegNumber = getVehicleRegNumber("ABCDEF");
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);

            fareCalculatorService.calculateFare(ticket,false);
            ticketDAO.updateTicket(ticket); {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                //verifier que update ticket est true
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            }else{
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }

    @Test
    public void processExitingBikeTest() {
        try{
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            // TODO Appeler le ticket DAO pour savoir si le vehicule est un habitué ou pas
            fareCalculatorService.calculateFare(ticket,false);
            if(ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                //verifier que update ticket est true
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            }else{
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }

        @Test
        public void getVehicleRegNumber () throws Exception {
            parkingService.processIncomingVehicle();

                //vérifier qu'une plaque d'immatriculation est bien entrée
                assertEquals(inputReaderUtil.readVehicleRegistrationNumber(),"ABCDEF");
            }



    }

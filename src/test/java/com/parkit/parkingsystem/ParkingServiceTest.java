package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        public void getVehicleRegNumber () throws Exception {
            parkingService.processIncomingVehicle();

                //vérifier qu'une plaque d'immatriculation est bien entrée
                assertEquals(inputReaderUtil.readVehicleRegistrationNumber(),"ABCDEF");
            }


     /*   @Test
        public void getNextParkingNumberIfAvailable () {
            parkingService.processIncomingVehicle();
            int parkingNumber=0;
            ParkingSpot parkingSpot = null;
            ParkingType parkingType = getVehicleType();
            parkingSpot = new ParkingSpot(parkingNumber,parkingType, true);
            //vérifier qu'on obtient bien la prochaine place disponible
            //int parkingNumber=0;
           //ParkingSpot parkingSpot = null;
            //ParkingType parkingType = getVehicleType();
         assertThat(parkingSpotDAO.ParkingSpot(parkingNumber,parkingType, true));

        }

        @Test
        public void getVehicleType () {

            parkingService.processIncomingVehicle();
           assertEquals(inputReaderUtil.readSelection(),1,2);
            //vérifier que le type du véhicule est bien entré

        }
/*

        // TODO ajouter un test pour une entrée

        @Test
        public void processExitingVehicleTest () {
            parkingService.processExitingVehicle();
            verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
            // Vérifier qu'on appelle bien le fareCalculatorService
        }

    }*/
    }

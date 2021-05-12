package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {


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
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);


            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processIncomingVehicleTest() {

        parkingService.processIncomingVehicle();
        //verifier les actions faites par le service (les identifier voir ce qu'ils font plus besoin des when verify et AssertThat)


    }
/*
        @Test
        public void getVehichleRegNumber () {
            parkingService.processIncomingVehicle();
            //vérifier qu'une plaque d'immatriculation est bien entrée
        }

        @Test
        public void getNextParkingNumberIfAvailable () {
            parkingService.processIncomingVehicle();
            //vérifier qu'on obtient bien la prochaine place disponible

        }

        @Test
        public void getVehichleType () {
            parkingService.processIncomingVehicle();
            System.out.println("Please select vehicle type from menu");
            System.out.println("1 CAR");
            System.out.println("2 BIKE");
            int input = inputReaderUtil.readSelection();
            //vérifier que le type du véhicule est bien entré

        }


        // TODO ajouter un test pour une entrée

        @Test
        public void processExitingVehicleTest () {
            parkingService.processExitingVehicle();
            verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
            // Vérifier qu'on appelle bien le fareCalculatorService
        }

    }
*/}

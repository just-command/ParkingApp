package com.example.parkingapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParkingSpotTest {

    @Test
    public void testParkingSpotCreation() {
        // Создаем свободное парковочное место
        ParkingSpot spot = new ParkingSpot("A1", false);
        
        // Проверяем начальное состояние
        assertEquals("Номер места должен соответствовать заданному", 
                    "A1", spot.getSpotNumber());
        assertFalse("Место должно быть свободным", spot.isOccupied());
    }

    @Test
    public void testOccupiedParkingSpotCreation() {
        // Создаем занятое парковочное место
        ParkingSpot spot = new ParkingSpot("A2", true);
        
        // Проверяем начальное состояние
        assertEquals("Номер места должен соответствовать заданному", 
                    "A2", spot.getSpotNumber());
        assertTrue("Место должно быть занятым", spot.isOccupied());
    }

    @Test
    public void testSetOccupied() {
        // Создаем свободное парковочное место
        ParkingSpot spot = new ParkingSpot("A1", false);
        
        // Проверяем начальное состояние
        assertFalse("Место должно быть свободным", spot.isOccupied());
        
        // Занимаем место
        spot.setOccupied(true);
        assertTrue("Место должно стать занятым", spot.isOccupied());
        
        // Освобождаем место
        spot.setOccupied(false);
        assertFalse("Место должно стать свободным", spot.isOccupied());
    }

    @Test
    public void testMultipleStateChanges() {
        // Создаем парковочное место
        ParkingSpot spot = new ParkingSpot("A1", false);
        
        // Проверяем несколько изменений состояния
        for (int i = 0; i < 3; i++) {
            // Занимаем место
            spot.setOccupied(true);
            assertTrue("Место должно быть занятым после установки true", 
                      spot.isOccupied());
            
            // Освобождаем место
            spot.setOccupied(false);
            assertFalse("Место должно быть свободным после установки false", 
                       spot.isOccupied());
        }
    }

    @Test
    public void testSpotNumberImmutability() {
        // Создаем парковочное место
        ParkingSpot spot = new ParkingSpot("A1", false);
        
        // Проверяем, что номер места не меняется при изменении состояния
        spot.setOccupied(true);
        assertEquals("Номер места не должен меняться при изменении состояния", 
                    "A1", spot.getSpotNumber());
        
        spot.setOccupied(false);
        assertEquals("Номер места не должен меняться при изменении состояния", 
                    "A1", spot.getSpotNumber());
    }

    @Test
    public void testSpotNumberCannotChange() {
        // Проверяем, что номер места остается неизменным
        ParkingSpot spot = new ParkingSpot("B2", true);

        // Меняем состояние несколько раз
        spot.setOccupied(false);
        spot.setOccupied(true);
        spot.setOccupied(false);

        // Номер должен остаться прежним
        assertEquals("B2", spot.getSpotNumber());
    }
} 
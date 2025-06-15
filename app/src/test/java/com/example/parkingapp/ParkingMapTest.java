package com.example.parkingapp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParkingMapTest {
    private ParkingMap parkingMap;
    private static final String MAP_NAME = "Test Map";
    private static final String SVG_FILE = "test.svg";

    @Before
    public void setUp() {
        parkingMap = new ParkingMap(MAP_NAME, SVG_FILE);
    }

    @Test
    public void testInitialization() {
        // Проверяем корректность инициализации
        assertEquals("Имя карты должно соответствовать заданному", MAP_NAME, parkingMap.getMapName());
        assertEquals("Имя SVG файла должно соответствовать заданному", SVG_FILE, parkingMap.getSvgFileName());
        assertTrue("Список парковочных мест должен быть пустым при инициализации", 
                  parkingMap.getParkingSpots().isEmpty());
    }

    @Test
    public void testAddParkingSpot() {
        // Создаем тестовое парковочное место
        ParkingSpot spot = new ParkingSpot("A1", false);
        
        // Добавляем место на карту
        parkingMap.addParkingSpot(spot);
        
        // Проверяем, что место было добавлено
        assertEquals("Должно быть добавлено одно парковочное место", 
                    1, parkingMap.getParkingSpots().size());
        assertEquals("Добавленное место должно соответствовать созданному", 
                    spot, parkingMap.getParkingSpots().get(0));
    }

    @Test
    public void testAddsParkingSpot() {
        ParkingSpot spot = new ParkingSpot("B1", false);
        parkingMap.addParkingSpot(spot);

        assertEquals("Количество мест должно быть 1", 1, parkingMap.getParkingSpots().size());
        assertEquals("Добавленное место должно быть 'B1'", spot, parkingMap.getParkingSpots().get(0));
    }

    @Test
    public void testGetParkingSpot() {
        // Создаем тестовые парковочные места
        ParkingSpot spot1 = new ParkingSpot("A1", false);
        ParkingSpot spot2 = new ParkingSpot("A2", true);
        
        // Добавляем места на карту
        parkingMap.addParkingSpot(spot1);
        parkingMap.addParkingSpot(spot2);

        // Проверяем поиск существующих мест
        assertEquals("Должно найтись место A1", spot1, parkingMap.getParkingSpot("A1"));
        assertEquals("Должно найтись место A2", spot2, parkingMap.getParkingSpot("A2"));
        
        // Проверяем поиск несуществующего места
        assertNull("Не должно найтись несуществующее место", 
                  parkingMap.getParkingSpot("B1"));
    }

    @Test
    public void testMultipleParkingSpots() {
        // Добавляем несколько парковочных мест
        for (int i = 1; i <= 5; i++) {
            parkingMap.addParkingSpot(new ParkingSpot("A" + i, false));
        }

        // Проверяем общее количество мест
        assertEquals("Должно быть 5 парковочных мест", 
                    5, parkingMap.getParkingSpots().size());

        // Проверяем поиск каждого места
        for (int i = 1; i <= 5; i++) {
            ParkingSpot spot = parkingMap.getParkingSpot("A" + i);
            assertNotNull("Место A" + i + " должно существовать", spot);
            assertEquals("Номер места должен соответствовать", 
                        "A" + i, spot.getSpotNumber());
        }
    }
} 
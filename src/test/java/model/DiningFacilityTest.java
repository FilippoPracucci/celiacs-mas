package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiningFacilityTest {
    private static final FacilityId FACILITY_ID = new FacilityId("1");
    private static final Position FACILITY_POSITION = new Position(0, 0);
    private static final int MAX_CAPACITY = 10;

    private DiningFacility diningFacility;

    @BeforeEach
    public void setUp() {
        this.diningFacility = new DiningFacilityImpl(FACILITY_ID, FACILITY_POSITION, MAX_CAPACITY);
    }

    @Test
    public void testGetId() {
        assertEquals(FACILITY_ID, this.diningFacility.getId());
    }

    @Test
    public void testGetPosition() {
        assertEquals(FACILITY_POSITION, this.diningFacility.getPosition());
    }

    @Test
    public void testGetMaxCapacity() {
        assertEquals(MAX_CAPACITY, this.diningFacility.getMaxCapacity());
    }

    @Test
    @DisplayName("Test making a reservation with available capacity reduces the capacity correctly")
    public void testMakeReservationWithAvailableCapacity() {
        this.diningFacility.makeReservation();
        assertEquals(MAX_CAPACITY - 1, this.diningFacility.getActualCapacity());
    }

    @Test
    @DisplayName("Test making a reservation with no available capacity fails")
    public void testMakeReservationWithNoAvailableCapacity() {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            this.diningFacility.makeReservation();
        }
        assertThrows(IllegalStateException.class, () -> this.diningFacility.makeReservation());
    }

    @Test
    @DisplayName("Test canceling a reservation increases the capacity correctly")
    public void testCancelReservation() {
        this.diningFacility.makeReservation();
        this.diningFacility.cancelReservation();
        assertEquals(MAX_CAPACITY, this.diningFacility.getActualCapacity());
    }

    @Test
    @DisplayName("Test canceling a reservation when there are no reservations fails")
    public void testCancelReservationWithNoReservations() {
        assertThrows(IllegalStateException.class, () -> this.diningFacility.cancelReservation());
    }
}

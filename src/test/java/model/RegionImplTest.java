package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RegionImplTest {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final FacilityId SHOPPING_FACILITY_ID = new FacilityId("shopping1");
    private static final FacilityId DINING_FACILITY_ID = new FacilityId("dining1");
    private static final UserId USER_ID = new UserId("user1");
    private static final FacilityId NON_EXISTENT_FACILITY_ID = new FacilityId("nonexistent");
    private static final UserId NON_EXISTENT_USER_ID = new UserId("nonexistent");
    private static final Position SHOPPING_FACILITY_POSITION = new Position(0, 0);
    private static final Position DINING_FACILITY_POSITION = new Position(5, 5);
    private static final Position USER_POSITION = new Position(1, 1);
    private static final int INITIAL_QUANTITY = 10;
    private static final int MAX_RESERVATIONS = 10;

    private Region region;
    private Set<City> cities;
    private Set<ShoppingFacility> shoppingFacilities;
    private Set<DiningFacility> diningFacilities;
    private final Map<UserId, Position> users = new HashMap<>();

    @BeforeEach
    public void setUp() {
        this.cities = Set.of(
                new City("city1", new Position(0, 0), new Position(2, 2)),
                new City("city2", new Position(3, 3), new Position(5, 5))
        );
        this.shoppingFacilities = Set.of(
                new ShoppingFacilityImpl(SHOPPING_FACILITY_ID, SHOPPING_FACILITY_POSITION, INITIAL_QUANTITY)
        );
        this.diningFacilities = Set.of(
                new DiningFacilityImpl(DINING_FACILITY_ID, DINING_FACILITY_POSITION, MAX_RESERVATIONS)
        );
        this.users.put(USER_ID, USER_POSITION);
        this.region = new RegionImpl(WIDTH, HEIGHT, this.cities, this.shoppingFacilities, this.diningFacilities,
                this.users);
    }

    @Test
    @DisplayName("Test creating a region with cities out of bounds fails")
    public void testRegionCreationWithOutOfBoundsCities() {
        Set<City> outOfBoundsCities = Set.of(
                new City("city1", new Position(1, 12), new Position(1, 12)),
                new City("city2", new Position(3, 3), new Position(11, 5))
        );
        assertThrows(IllegalArgumentException.class, () ->
                new RegionImpl(WIDTH, HEIGHT, outOfBoundsCities, this.shoppingFacilities, this.diningFacilities,
                        this.users)
        );
    }

    @Test
    @DisplayName("Test retrieving region dimensions")
    public void testGetDimensions() {
        assertEquals(WIDTH, this.region.getWidth());
        assertEquals(HEIGHT, this.region.getHeight());
    }

    @Test
    @DisplayName("Test retrieving shopping facility position from a region")
    public void testGetShoppingFacilityPosition() {
        assertEquals(SHOPPING_FACILITY_POSITION, this.region.getShoppingFacilityPosition(SHOPPING_FACILITY_ID));
        assertThrows(IllegalArgumentException.class, () ->
                this.region.getShoppingFacilityPosition(NON_EXISTENT_FACILITY_ID)
        );
    }

    @Test
    @DisplayName("Test retrieving dining facility position from a region")
    public void testGetDiningFacilityPosition() {
        assertEquals(DINING_FACILITY_POSITION, this.region.getDiningFacilityPosition(DINING_FACILITY_ID));
        assertThrows(IllegalArgumentException.class, () ->
                this.region.getDiningFacilityPosition(NON_EXISTENT_FACILITY_ID)
        );
    }

    @Test
    @DisplayName("Test retrieving user position from a region")
    public void testGetUserPosition() {
        assertEquals(USER_POSITION, this.region.getUserPosition(USER_ID));
        assertThrows(IllegalArgumentException.class, () ->
                this.region.getUserPosition(NON_EXISTENT_USER_ID)
        );
    }

    @Test
    @DisplayName("Test moving a user in the region")
    public void testMoveUser() {
        Position initialPosition = this.region.getUserPosition(USER_ID);
        assertTrue(this.region.moveUser(USER_ID, Direction.NORTH));
        assertEquals(new Position(initialPosition.x(), initialPosition.y() - 1), this.region.getUserPosition(USER_ID));
    }

    @Test
    @DisplayName("Test moving a user out of bounds fails")
    public void testMoveUserOutOfBounds() {
        while (this.region.getUserPosition(USER_ID).y() > 0) {
            this.region.moveUser(USER_ID, Direction.NORTH);
        }
        assertFalse(this.region.moveUser(USER_ID, Direction.NORTH));
    }

    @Test
    @DisplayName("Test purchase a quantity of products from a shopping facility")
    public void testPurchase() {
        final int purchaseQuantity = 5;
        assertTrue(this.region.purchase(USER_ID, SHOPPING_FACILITY_ID, purchaseQuantity));
        assertEquals(INITIAL_QUANTITY - purchaseQuantity, this.getShoppingFacilityById().getQuantity());
    }

    @Test
    @DisplayName("Test purchase fails when facility is not present or does not have enough quantity")
    public void testPurchaseFails() {
        final int purchaseQuantity = 5;
        assertFalse(this.region.purchase(USER_ID, NON_EXISTENT_FACILITY_ID, purchaseQuantity));
            assertFalse(this.region.purchase(USER_ID, SHOPPING_FACILITY_ID,
                    this.getShoppingFacilityById().getQuantity() + 1));
    }

    @Test
    @DisplayName("Test supplying a shopping facility increases its quantity")
    public void testSupplyFacility() {
        final int supplyQuantity = 5;
        this.region.supplyFacility(SHOPPING_FACILITY_ID, supplyQuantity);
        assertEquals(INITIAL_QUANTITY + supplyQuantity, this.getShoppingFacilityById().getQuantity());
    }

    @Test
    @DisplayName("Test supplying a shopping facility with non-positive quantity fails")
    public void testSupplyFacilityFailsWithNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () ->
                this.region.supplyFacility(SHOPPING_FACILITY_ID, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                this.region.supplyFacility(SHOPPING_FACILITY_ID, -5)
        );
    }

    @Test
    @DisplayName("Test making a reservation at a dining facility")
    public void testMakeReservation() {
        assertTrue(this.region.makeReservation(USER_ID, DINING_FACILITY_ID));
        assertFalse(this.region.makeReservation(USER_ID, NON_EXISTENT_FACILITY_ID));
        for (int i = 0; i < MAX_RESERVATIONS; i++) {
            this.region.makeReservation(USER_ID, DINING_FACILITY_ID);
        }
        assertFalse(this.region.makeReservation(USER_ID, DINING_FACILITY_ID));
    }

    private ShoppingFacility getShoppingFacilityById() {
        return this.shoppingFacilities
                .stream()
                .filter(f -> f.getId().equals(RegionImplTest.SHOPPING_FACILITY_ID))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Shopping facility not found"));
    }
}

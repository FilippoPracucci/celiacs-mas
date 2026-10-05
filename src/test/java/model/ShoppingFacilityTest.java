package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShoppingFacilityTest {
    private static final FacilityId FACILITY_ID = new FacilityId("1");
    private static final Position FACILITY_POSITION = new Position(0, 0);
    private static final int INITIAL_QUANTITY = 10;

    private ShoppingFacility shoppingFacility;

    @BeforeEach
    public void setUp() {
        this.shoppingFacility = new ShoppingFacilityImpl(FACILITY_ID, FACILITY_POSITION, INITIAL_QUANTITY);
    }

    @Test
    public void testGetId() {
        assertEquals(FACILITY_ID, this.shoppingFacility.getId());
    }

    @Test
    public void testGetPosition() {
        assertEquals(FACILITY_POSITION, this.shoppingFacility.getPosition());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(INITIAL_QUANTITY, this.shoppingFacility.getQuantity());
    }

    @Test
    @DisplayName("Test purchasing with enough stock reduces the quantity correctly")
    public void testPurchaseWithEnoughStock() {
        int purchaseQuantity = INITIAL_QUANTITY / 2;
        this.shoppingFacility.purchase(purchaseQuantity);
        assertEquals(INITIAL_QUANTITY - purchaseQuantity, this.shoppingFacility.getQuantity());
    }

    @Test
    @DisplayName("Test purchasing with not enough stock fails")
    public void testPurchaseWithNotEnoughStock() {
        int purchaseQuantity = INITIAL_QUANTITY + 1;
        assertThrows(IllegalArgumentException.class, () -> this.shoppingFacility.purchase(purchaseQuantity));
    }

    @Test
    @DisplayName("Test supplying items increases the quantity correctly")
    public void testSupply() {
        int supplyQuantity = 5;
        this.shoppingFacility.supply(supplyQuantity);
        assertEquals(INITIAL_QUANTITY + supplyQuantity, this.shoppingFacility.getQuantity());
    }
}

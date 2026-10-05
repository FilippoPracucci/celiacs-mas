package model;

public class ShoppingFacilityImpl implements ShoppingFacility {
    private final FacilityId id;
    private final Position position;
    private int quantity;

    public ShoppingFacilityImpl(final FacilityId id, final Position position, final int quantity) {
        this.id = id;
        this.position = position;
        this.quantity = quantity;
    }

    @Override
    public FacilityId getId() {
        return this.id;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void purchase(final int quantity) {
        if  (quantity <= this.quantity) {
            this.quantity -= quantity;
        } else {
            throw new IllegalArgumentException("Not enough stock available for purchase.");
        }
    }

    @Override
    public void supply(final int quantity) {
        this.quantity += quantity;
    }
}

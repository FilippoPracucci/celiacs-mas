package model;

public interface ShoppingFacility extends Facility {
    int getQuantity();

    void purchase(int quantity);

    void supply(int quantity);
}

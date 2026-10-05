package model;

public interface DiningFacility extends Facility {
    int getMaxCapacity();

    int getActualCapacity();

    void makeReservation();

    void cancelReservation();
}

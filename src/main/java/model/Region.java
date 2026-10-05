package model;

public interface Region {

    int getWidth();

    int getHeight();

    Position getShoppingFacilityPosition(FacilityId facilityId);

    Position getDiningFacilityPosition(FacilityId facilityId);

    Position getUserPosition(UserId userId);

    boolean moveUser(UserId userId, Direction direction);

    boolean purchase(UserId userId, FacilityId facilityId, int quantity);

    boolean makeReservation(UserId userId, FacilityId facilityId);

    void supplyFacility(FacilityId facilityId, int quantity);
}

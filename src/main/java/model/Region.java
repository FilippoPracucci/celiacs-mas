package model;

public interface Region {

    int getWidth();

    int getHeight();

    Position getShoppingFacilityPosition(FacilityId facilityId);

    int getShoppingFacilityStockQuantity(FacilityId facilityId);

    Position getDiningFacilityPosition(FacilityId facilityId);

    int getDiningFacilityCapacity(FacilityId facilityId);

    Position getUserPosition(UserId userId);

    boolean moveUser(UserId userId, Direction direction, Position destination);

    boolean moveUser(UserId userId, Direction direction);

    boolean enterFacility(UserId userId, FacilityId facilityId);

    boolean exitFacility(UserId userId, FacilityId facilityId);

    boolean purchase(UserId userId, FacilityId facilityId, int quantity);

    boolean makeReservation(UserId userId, FacilityId facilityId);

    boolean supplyFacility(FacilityId facilityId, int quantity);

    String getUserCurrentCity(UserId userId);
}

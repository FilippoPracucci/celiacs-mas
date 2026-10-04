package model;

public interface Region {

    Position getFacilityPosition(FacilityId facilityId);

    Position getUserPosition(UserId userId);

    boolean purchase(UserId userId, FacilityId facilityId, int quantity);

    boolean makeReservation(UserId userId, FacilityId facilityId);

    void supplyFacility(FacilityId facilityId, int quantity);
}

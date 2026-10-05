package model;

import java.util.*;

public class RegionImpl implements Region {

    private final int width;
    private final int height;
    private final Set<City> cities;
    private final Set<ShoppingFacility> shoppingFacilities;
    private final Set<DiningFacility> diningFacilities;
    private final Map<UserId, Position> users;

    public RegionImpl(final int width, final int height, final Set<City> cities,
                      final Set<ShoppingFacility> shoppingFacilities, final Set<DiningFacility> diningFacilities,
                      final Map<UserId, Position> users) {
        this.width = width;
        this.height = height;
        this.shoppingFacilities = shoppingFacilities;
        this.diningFacilities = diningFacilities;
        this.users = users;
        this.cities = cities;
        this.validateCitiesPositions();
    }

    private void validateCitiesPositions() {
        for (final City city : this.cities) {
            if (isPositionOutOfBounds(city.startVertex()) || isPositionOutOfBounds(city.endVertex())) {
                throw new IllegalArgumentException("City " + city.id() + " has vertices out of region bounds.");
            }
        }
    }

    private boolean isPositionOutOfBounds(final Position position) {
        return position.x() < 0 || position.y() < 0 || position.x() > this.width || position.y() > this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Position getShoppingFacilityPosition(final FacilityId facilityId) {
        return this.getFacilityByIdFrom(this.shoppingFacilities, facilityId).getPosition();
    }

    @Override
    public Position getDiningFacilityPosition(final FacilityId facilityId) {
        return this.getFacilityByIdFrom(this.diningFacilities, facilityId).getPosition();
    }

    @Override
    public Position getUserPosition(final UserId userId) {
        if (this.users.containsKey(userId)) {
            return this.users.get(userId);
        }
        throw new IllegalArgumentException("User with ID " + userId + " not found in the region.");
    }

    @Override
    public boolean purchase(final UserId userId, final FacilityId facilityId, final int quantity) {
        this.requireUser(userId);
        try {
            this.getFacilityByIdFrom(this.shoppingFacilities, facilityId).purchase(quantity);
        } catch (final IllegalArgumentException e) {
            System.out.println("Error during purchase: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean makeReservation(final UserId userId, final FacilityId facilityId) {
        this.requireUser(userId);
        try {
            this.getFacilityByIdFrom(this.diningFacilities, facilityId).makeReservation();
        } catch (final IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error during reservation: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void supplyFacility(final FacilityId facilityId, final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Supply quantity must be positive.");
        }
        this.getFacilityByIdFrom(this.shoppingFacilities, facilityId).supply(quantity);
    }

    private <T extends Facility> T getFacilityByIdFrom(final Set<T> facilities, final FacilityId facilityId) {
        return facilities.stream()
                .filter(f -> f.getId().equals(facilityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Facility with ID " + facilityId + " not found in the region."
                ));
    }

    private void requireUser(final UserId userId) {
        if (!this.users.containsKey(userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " not found in the region.");
        }
    }
}

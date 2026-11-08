package model;

import java.util.*;

public class RegionImpl implements Region {

    private final int width;
    private final int height;
    private final Set<City> cities;
    private final Map<ShoppingFacility, Position> shoppingFacilities;
    private final Map<DiningFacility, Position> diningFacilities;
    private final Map<UserId, Position> users;

    public RegionImpl(final int width, final int height, final Set<City> cities,
            final Map<ShoppingFacility, Position> shoppingFacilities,
            final Map<DiningFacility, Position> diningFacilities, final Map<UserId, Position> users
    ) {
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
        return position.x() < 0 || position.y() < 0 || position.x() >= this.width || position.y() >= this.height;
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
        return this.getFacilityPositionById(this.shoppingFacilities, facilityId);
    }

    @Override
    public Position getDiningFacilityPosition(final FacilityId facilityId) {
        return this.getFacilityPositionById(this.diningFacilities, facilityId);
    }

    @Override
    public Position getUserPosition(final UserId userId) {
        if (this.users.containsKey(userId)) {
            return this.users.get(userId);
        }
        throw new IllegalArgumentException("User with ID " + userId + " not found in the region.");
    }

    @Override
    public boolean moveUser(final UserId userId, final Direction direction) {
        this.requireUser(userId);
        final Position newPosition = this.users.get(userId).moveTowards(direction);
        if (this.isPositionOutOfBounds(newPosition) || this.isPositionOccupied(newPosition)) {
            return false;
        }
        this.users.replace(userId, newPosition);
        return true;
    }

    @Override
    public boolean moveUser(final UserId userId, final Direction direction, final Position destination) {
        this.requireUser(userId);
        final Position newPosition = this.users.get(userId).moveTowards(direction);
        if (
            !this.isPositionOutOfBounds(newPosition) &&
            newPosition.equals(destination) &&
            this.isPositionOccupied(newPosition)
        ) {
            this.users.replace(userId, newPosition);
            return true;
        }
        return this.moveUser(userId, direction);
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
    public boolean supplyFacility(final FacilityId facilityId, final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Supply quantity must be positive.");
        }
        this.getFacilityByIdFrom(this.shoppingFacilities, facilityId).supply(quantity);
        return true;
    }

    @Override
    public String toString() {
        return "RegionImpl{" +
                "width=" + width +
                ", height=" + height +
                ", cities=" + cities +
                ", shoppingFacilities=" + shoppingFacilities +
                ", diningFacilities=" + diningFacilities +
                ", users=" + users +
                '}';
    }

    @Override
    public String getUserCurrentCity(final UserId userId) {
        this.requireUser(userId);
        return this.cities.stream()
                .filter(city -> city.contains(this.users.get(userId)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User is not located in any city."))
                .id();
    }

    @Override
    public int getShoppingFacilityStockQuantity(final FacilityId facilityId) {
        return this.getFacilityByIdFrom(this.shoppingFacilities, facilityId).getQuantity();
    }

    @Override
    public int getDiningFacilityCapacity(final FacilityId facilityId) {
        return this.getFacilityByIdFrom(this.diningFacilities, facilityId).getActualCapacity();
    }

    private <T extends Facility> T getFacilityByIdFrom(final Map<T, Position> facilities, final FacilityId facilityId) {
        return facilities.keySet().stream()
                .filter(f -> f.getId().equals(facilityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Facility with ID " + facilityId + " not found in the region."
                ));
    }

    private <T extends Facility> Position getFacilityPositionById(
        final Map<T, Position> facilities,
        final FacilityId facilityId
    ) {
        return facilities.entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(facilityId))
                .map(Map.Entry::getValue)
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

    private boolean isPositionOccupied(final Position position) {
        return this.shoppingFacilities.containsValue(position) || this.diningFacilities.containsValue(position);
    }
}

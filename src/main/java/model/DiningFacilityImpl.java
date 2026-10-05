package model;

public class DiningFacilityImpl implements DiningFacility {
    private final FacilityId id;
    private final Position position;
    private final int maxCapacity;
    private int actualCapacity;

    public DiningFacilityImpl(final FacilityId id, final Position position, final int maxCapacity) {
        this.id = id;
        this.position = position;
        this.maxCapacity = maxCapacity;
        this.actualCapacity = maxCapacity;
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
    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    @Override
    public int getActualCapacity() {
        return this.actualCapacity;
    }

    @Override
    public void makeReservation() {
        if (this.actualCapacity > 0) {
            this.actualCapacity--;
        } else {
            throw new IllegalStateException("No capacity available for new reservations.");
        }
    }

    @Override
    public void cancelReservation() {
        if (this.actualCapacity == this.maxCapacity) {
            throw new IllegalStateException("No reservations to cancel.");
        } else {
            this.actualCapacity++;
        }
    }
}

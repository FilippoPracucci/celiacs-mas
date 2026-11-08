package model;

public class DiningFacilityImpl implements DiningFacility {

    private final FacilityId id;
    private final int maxCapacity;
    private int actualCapacity;

    public DiningFacilityImpl(final FacilityId id, final int maxCapacity) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.actualCapacity = maxCapacity;
    }

    @Override
    public FacilityId getId() {
        return this.id;
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

    @Override
    public String toString() {
        return "DiningFacilityImpl{" +
                "id=" + id +
                ", maxCapacity=" + maxCapacity +
                ", actualCapacity=" + actualCapacity +
                '}';
    }
}

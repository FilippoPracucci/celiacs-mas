package model;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public static Direction fromString(final String direction) {
        return Direction.valueOf(direction.toUpperCase());
    }
}

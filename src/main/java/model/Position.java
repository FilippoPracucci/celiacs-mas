package model;

public record Position(int x, int y) {

    public Position moveTowards(final Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(this.x, this.y - 1);
            case SOUTH -> new Position(this.x, this.y + 1);
            case EAST -> new Position(this.x + 1, this.y);
            case WEST -> new Position(this.x - 1, this.y);
        };
    }
}

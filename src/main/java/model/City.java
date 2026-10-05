package model;

import java.util.Collection;
import java.util.stream.IntStream;

public record City(String id, Position startVertex, Position endVertex) {

    public City {
        if (startVertex.x() > endVertex.x() || startVertex.y() > endVertex.y()) {
            throw new IllegalArgumentException(
                "Start vertex coordinates must be less than or equal to end vertex coordinates."
            );
        }
        if (startVertex.x() < 0 || startVertex.y() < 0) {
            throw new IllegalArgumentException("Start vertex coordinates must be non-negative.");
        }
    }

    public Collection<Position> getAllPositions() {
        return IntStream.rangeClosed(this.startVertex.x(), this.endVertex.x())
                .boxed()
                .flatMap(x ->
                    IntStream.rangeClosed(this.startVertex.y(), this.endVertex.y()).mapToObj(y -> new Position(x, y))
                )
                .toList();
    }

    public boolean contains(final Position position) {
        return this.getAllPositions().contains(position);
    }
}

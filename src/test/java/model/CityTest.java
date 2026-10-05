package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    private static final String CITY_ID = "city1";
    private static final Position START_VERTEX = new Position(0, 0);
    private static final Position END_VERTEX = new Position(2, 4);

    private City city;

    @BeforeEach
    public void setUp() {
        this.city = new City(CITY_ID, START_VERTEX, END_VERTEX);
    }

    @Test
    @DisplayName("Test creation of a City with start vertex greater than end vertex fails")
    public void testIllegalCreation() {
        assertThrows(IllegalArgumentException.class, () ->
            new City(CITY_ID, new Position(3, 3), new Position(2, 2))
        );
        assertThrows(IllegalArgumentException.class, () ->
                new City(CITY_ID, new Position(-1, -2), new Position(2, 2))
        );
    }

    @Test
    @DisplayName("Test get all positions within the city")
    public void testGetAllPositions() {
        assertEquals(getPositionsBetweenVertex(), city.getAllPositions());
    }

    @Test
    @DisplayName("Test city contains a position within its bounds")
    public void testCityContainsPosition() {
        assertTrue(city.contains(START_VERTEX));
        assertTrue(city.contains(END_VERTEX));
        assertTrue(city.contains(new Position(2, 3)));
    }

    @Test
    @DisplayName("Test city does not contain a position outside its bounds")
    public void testCityDoesNotContainPosition() {
        assertFalse(city.contains(new Position(-1, 0)));
        assertFalse(city.contains(new Position(0, -1)));
        assertFalse(city.contains(new Position(3, 3)));
    }

    private Collection<Position> getPositionsBetweenVertex() {
        return IntStream.rangeClosed(CityTest.START_VERTEX.x(), END_VERTEX.x())
                .boxed()
                .flatMap(x -> IntStream.rangeClosed(CityTest.START_VERTEX.y(), END_VERTEX.y())
                        .mapToObj(y -> new Position(x, y))
                )
                .toList();

    }
}

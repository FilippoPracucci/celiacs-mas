package model;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RegionLoaderFromJsonFile implements LoaderFromFile<Region> {

    @Override
    public Region load(final String filePath) {
        try (final FileInputStream inputStream = new FileInputStream(filePath)) {;
            return parseRegionFromJsonObject(Json.createReader(inputStream).readObject());
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load region from file: " + filePath, e);
        }
    }

    private Region parseRegionFromJsonObject(final JsonObject jsonObject) {
        return new RegionImpl(
                jsonObject.getInt("width"),
                jsonObject.getInt("height"),
                parseCitiesFromJsonArray(jsonObject.getJsonArray("cities")),
                parseShoppingFacilitiesFromJsonArray(jsonObject.getJsonArray("shoppingFacilities")),
                parseDiningFacilitiesFromJsonArray(jsonObject.getJsonArray("diningFacilities")),
                parseUsersFromJsonArray(jsonObject.getJsonArray("users"))
        );
    }

    private Set<City> parseCitiesFromJsonArray(final JsonArray cities) {
        return cities.stream()
                .map(JsonValue::asJsonObject)
                .map(city -> new City(
                        city.getString("id"),
                        new Position(city.getInt("startX"), city.getInt("startY")),
                        new Position(city.getInt("endX"), city.getInt("endY"))
                ))
                .collect(Collectors.toSet());
    }

    private Set<ShoppingFacility> parseShoppingFacilitiesFromJsonArray(final JsonArray shoppingFacilities) {
        return shoppingFacilities.stream()
                .map(JsonValue::asJsonObject)
                .map(shoppingFacility -> new ShoppingFacilityImpl(
                        new FacilityId(shoppingFacility.getString("id")),
                        new Position(shoppingFacility.getInt("x"), shoppingFacility.getInt("y")),
                        shoppingFacility.getInt("quantity")
                ))
                .collect(Collectors.toSet());
    }

    private Set<DiningFacility> parseDiningFacilitiesFromJsonArray(final JsonArray diningFacilities) {
        return diningFacilities.stream()
                .map(JsonValue::asJsonObject)
                .map(diningFacility -> new DiningFacilityImpl(
                        new FacilityId(diningFacility.getString("id")),
                        new Position(diningFacility.getInt("x"), diningFacility.getInt("y")),
                        diningFacility.getInt("capacity")
                ))
                .collect(Collectors.toSet());
    }

    private Map<UserId, Position> parseUsersFromJsonArray(final JsonArray users) {
        return users.stream()
                .map(JsonValue::asJsonObject)
                .collect(Collectors.toMap(
                        user -> new UserId(user.getString("id")),
                        user -> new Position(user.getInt("x"), user.getInt("y"))
                ));
    }
}

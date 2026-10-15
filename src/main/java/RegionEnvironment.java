import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import model.*;

public class RegionEnvironment extends Environment {

    private static final String MOVE = "move";
    private static final String MAKE_RESERVATION = "make_reservation";
    private static final String PURCHASE = "purchase";
    private static final String SUPPLY_FACILITY = "supply_facility";

    private Region region = null;

    @Override
    public void init(final String[] args) {
        this.region = new RegionLoaderFromJsonFile().load(args[0]);
    }

    @Override
    public boolean executeAction(final String agentName, final Structure action) {
        return switch (action.getFunctor()) {
            case MOVE -> this.region.moveUser(new UserId(agentName), Direction.valueOf(action.getTerm(0).toString()));
            case PURCHASE -> this.region.purchase(
                    new UserId(action.getTerm(0).toString()),
                    new FacilityId(action.getTerm(1).toString()),
                    Integer.parseInt(action.getTerm(2).toString())
            );
            case SUPPLY_FACILITY -> {
                this.region.supplyFacility(
                    new FacilityId(action.getTerm(0).toString()),
                    Integer.parseInt(action.getTerm(1).toString())
                );
                yield true;
            }
            case MAKE_RESERVATION -> this.region.makeReservation(
                    new UserId(action.getTerm(0).toString()),
                    new FacilityId(action.getTerm(1).toString())
            );
            default -> false;
        };
    }

    @Override
    public Collection<Literal> getPercepts(final String agentName) {
        return switch (agentName.charAt(0)) {
            case 'c' -> Collections.emptyList();
            case 'u' -> this.getUserPercepts(new UserId(agentName));
            case 's' -> this.getShoppingFacilityPercepts(new FacilityId(agentName));
            case 'd' -> this.getDiningFacilityPercepts(new FacilityId(agentName));
            default -> throw new IllegalArgumentException("Unknown agent: " + agentName);
        };
    }

    private Collection<Literal> getUserPercepts(final UserId userId) {
        return List.of(
            Literal.parseLiteral(String.format("position(%d, %d)",
                this.region.getUserPosition(userId).x(),
                this.region.getUserPosition(userId).y()
            )),
            Literal.parseLiteral(String.format("current_city(%s)", this.region.getUserCurrentCity(userId)))
        );
    }

    private Collection<Literal> getShoppingFacilityPercepts(final FacilityId facilityId) {
        return List.of(
            Literal.parseLiteral(String.format("position(%d, %d)",
                this.region.getShoppingFacilityPosition(facilityId).x(),
                this.region.getShoppingFacilityPosition(facilityId).y()
            )),
            Literal.parseLiteral(String.format("quantity(%d)",
                this.region.getShoppingFacilityStockQuantity(facilityId)
            ))
        );
    }

    private Collection<Literal> getDiningFacilityPercepts(final FacilityId facilityId) {
        return List.of(
            Literal.parseLiteral(String.format("position(%d, %d)",
                this.region.getDiningFacilityPosition(facilityId).x(),
                this.region.getDiningFacilityPosition(facilityId).y()
            )),
            Literal.parseLiteral(String.format("capacity(%d)",
                this.region.getDiningFacilityCapacity(facilityId)
            ))
        );
    }
}

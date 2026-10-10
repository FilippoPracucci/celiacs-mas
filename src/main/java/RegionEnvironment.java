import java.util.Collection;
import java.util.List;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import model.*;

public class RegionEnvironment extends Environment {

    private static final String MOVE = "move";
    private static final String MAKE_RESERVATION = "make_reservation";
    private static final String PURCHASE = "purchase";

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
            case 'c' -> List.of();
            case 'u' -> List.of();
            case 's' -> List.of();
            case 'd' -> List.of();
            default -> throw new IllegalArgumentException("Unknown agent: " + agentName);
        };
    }

}

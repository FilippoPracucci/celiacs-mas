/* Beliefs and rules */

city(c1).

/* Percepts */

+position(X, Y) : quantity(Quantity) & Quantity > 0 <-
    .print("Shopping Facility at: ", X, ", ", Y, " with ", Quantity, " items in stock").

+quantity(Quantity) : city(Agent) <-
    .print("Shopping Facility has ", Quantity, " items in stock");
    .send(Agent, achieve, update_facility_status(Quantity)).

/* Plans */

+!purchase(User, Quantity) : quantity(Q) <-
    ?stock_status(Quantity, sufficient);
    .print("Processing a purchase of ", Quantity, " items");
    .my_name(Agent);
    purchase(User, Agent, Quantity).

-!purchase(User, Quantity) : quantity(Q) <-
    ?stock_status(Quantity, insufficient);
    .print("Cannot process a purchase of ", Quantity, " items due to insufficient stock");
    !supply_stock(10).

+!supply_stock(Quantity) : quantity(Q) <-
    .print("Supplying ", Quantity, " items in stock");
    .my_name(Agent);
    supply_facility(Agent, Quantity).

/* Test goals */

+?shopping_facility_info(position(X, Y), Quantity) <-
    ?position(X, Y);
    ?quantity(Quantity).

+?stock_status(Quantity, sufficient)[source(Sender)] : Quantity > 0 <-
    ?quantity(Q) > 0;
    ?quantity(Q) >= Quantity;
    .print("Shopping Facility has sufficient stock").

+?stock_status(Quantity, insufficient)[source(Sender)] : Quantity > 0 & (quantity(Q) & Q <= 0 | Q < Quantity) <-
    .print("Shopping Facility has insufficient stock").

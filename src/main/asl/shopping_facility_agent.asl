/* Beliefs and rules */

city(c1).

/* Percepts */

+position(X, Y) : quantity(Quantity) & Quantity > 0 <-
    .print("Shopping Facility at: ", X, ", ", Y, " with ", Quantity, " items in stock").

+quantity(Quantity) : city(Agent) <-
    .print("Shopping Facility has ", Quantity, " items in stock");
    .send(Agent, achieve, update_facility_status(Quantity)).

/* Plans */

+!purchase(User, Quantity) : quantity(Q) & city(C) <-
    ?stock_status(Quantity);
    .print("Processing a purchase of ", Quantity, " items");
    .my_name(Agent);
    purchase(User, Agent, Quantity).

-!purchase(User, Quantity) : quantity(Q) & city(C) <-
    .print("Cannot process a purchase of ", Quantity, " items due to insufficient stock");
    .my_name(Agent);
    !supply_stock(10);
    .send(C, achieve, purchase_failed(User, Quantity)).

+!supply_stock(Quantity) : quantity(Q) <-
    .print("Supplying ", Quantity, " items in stock");
    .my_name(Agent);
    supply_facility(Agent, Quantity).

/* Test goals */

+?facility_info(position(X, Y), Quantity) <-
    ?position(X, Y);
    ?quantity(Quantity).

+?stock_status(Quantity) : Quantity > 0 & quantity(Q) <-
    if (Q >= Quantity) {
        .print("Shopping Facility has sufficient stock");
    } else {
        .print("Shopping Facility has insufficient stock");
        .fail;
    }.

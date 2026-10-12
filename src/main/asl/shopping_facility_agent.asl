/* Beliefs and rules */

available(quantity).

/* Percepts */

+position(X, Y) : available(Q) & Q > 0 <-
    .print("Shopping Facility at: ", X, ", ", Y, " with ", Q, " items in stock").

+stock(0) <-
    !supply_stock(10).

+!purchase(Quantity) : available(Q) & Q >= Quantity <-
    ?stock_status(sufficient);
    .print("Shopping Facility is processing a purchase of ", Quantity, " items");
    -+available(Q - Quantity);
    purchase(Quantity).

-!purchase(Quantity) : available(Q) & Q < Quantity <-
    ?stock_status(insufficient);
    .print("Shopping Facility cannot process a purchase of ", Quantity, " items due to insufficient stock").
    !supply_stock(10).

+stock_status(status)[source(Sender)] : city(Sender) & available(Quantity) <-
    .print("Shopping Facility has ", Quantity, " items in stock");
    .my_name(SF);
    .send(Sender, tell, Quantity).

+!supply_stock(Quantity) : available(Q) <-
    .print("Shopping Facility is supplying ", Quantity, " items in stock");
    -+available(Q + Quantity);
    supply(Quantity).

/* Test goals */

+?shopping_facility_info(Position(X, Y), Q) : position(X, Y) & available(Q) <-
    true.

+?stock_status(sufficient)[source(Sender)] : available(Q) & Q > 0 <-
    .print("Shopping Facility has sufficient stock").

+?stock_status(insufficient)[source(Sender)] : available(Q) <-
    .print("Shopping Facility has insufficient stock");
    !supply_stock(10).

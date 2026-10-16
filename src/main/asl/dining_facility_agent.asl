/* Beliefs and rules */
city(c1).

/* Percepts */

+position(X, Y) : capacity(C) & C > 0 <-
    .print("Dining Facility at: ", X, ", ", Y, " with capacity of ", C, " reservations").

+capacity(C) : city(Agent) <-
    .print("Dining Facility has a capacity of ", C, " reservations");
    .send(Agent, achieve, update_facility_status(C)).

/* Plans */

+!make_reservation(User) <-
    ?capacity_status(C, sufficient);
    .print("Processing a reservation for user ", User);
    .my_name(Agent);
    make_reservation(User, Agent).

-!make_reservation(User) <-
    ?capacity_status(C, insufficient);
    .print("Cannot process a reservation for user ", User, " due to full capacity").

/* Test goals */

+?facility_info(position(X, Y), C) <-
    ?position(X, Y);
    ?capacity(C).

+?capacity_status(C, sufficient)[source(Sender)] : C > 0 <-
    ?capacity(C) > 0;
    .print("Dining Facility has sufficient capacity for reservations").

+?capacity_status(C, insufficient)[source(Sender)] : C > 0 & (capacity(C) & C <= 0) <-
    .print("Dining Facility has insufficient capacity for reservations").

/* Beliefs and rules */
city(c1).

/* Percepts */

+position(X, Y) : capacity(C) & C > 0 <-
    .print("Dining Facility at: ", X, ", ", Y, " with capacity of ", C, " people").

+capacity(C) : city(Agent) <-
    .print("Dining Facility has a capacity of ", C, " people");
    .send(Agent, achieve, update_facility_status(C)).

/* Test goals */

+?dining_facility_info(position(X, Y), C) <-
    ?position(X, Y);
    ?capacity(C).

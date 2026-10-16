/* Beliefs and rules */

same_position(position(X1, Y1), position(X2, Y2)) :- X1 == X2 & Y1 == Y2.


/* Initial goals */

!purchase.
!make_reservation.


/* Percepts */

+position(X, Y) : home(_) <-
    .print("User is at home: position ", X, ", ", Y);
    +home(position(X, Y)).

+position(X, Y) <-
    .print("User is at position: ", X, ", ", Y).

+current_city(C) <-
    .print("User is currently in city: ", C).


/* Plans */

/* Plans for purchasing from a shopping facility */

+!purchase : current_city(C) & position(X, Y) <-
    .print("User is looking for a shopping facility");
    .send(C, askOne, closest_shopping_facility(position(X, Y), F, Facility_Position), Answer, 2000);
    !handle_purchase_response(Answer).

+!handle_purchase_response(closest_shopping_facility(_, F, Facility_Position)) : current_city(C) &
        same_position(position(X, Y), Facility_Position) <-
    .print("User found closest shopping facility: ", F, " at position ", Facility_Position);
    .my_name(User);
    .send(C, achieve, make_purchase(User, F, 5)).

+!handle_purchase_response(closest_shopping_facility(_, F, Facility_Position)) : current_city(C) &
        not(same_position(position(X, Y), Facility_Position)) <-
    .print("User moving towards the closest shopping facility: ", F, " at position ", Facility_Position).

/* Plans for making a reservation at a dining facility */

+!make_reservation : current_city(C) & position(X, Y) <-
    .print("User is looking for a dining facility");
    .send(C, askOne, closest_dining_facility(position(X, Y), F, Facility_Position), Answer, 2000);
    !handle_reservation_response(Answer).

+!handle_reservation_response(closest_dining_facility(_, F, Facility_Position)) : current_city(C) &
        same_position(position(X, Y), Facility_Position) <-
    .print("User found closest dining facility: ", F, " at position ", Facility_Position);
    .my_name(User);
    .send(C, achieve, make_reservation(User, F)).

+!handle_reservation_response(closest_dining_facility(_, F, Facility_Position)) : current_city(C) &
        not(same_position(position(X, Y), Facility_Position)) <-
    .print("User moving towards the closest dining facility: ", F, " at position ", Facility_Position).

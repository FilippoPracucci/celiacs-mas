/* Beliefs and rules */

same_position(position(X1, Y1), position(X2, Y2)) :- X1 == X2 & Y1 == Y2.

// direction(+StartPosition, +EndPosition, -PrimaryDirection, -SecondaryDirection)
directions(position(StartX, StartY), position(EndX, EndY), north, east) :- StartX < EndX & StartY > EndY.
directions(position(StartX, StartY), position(EndX, EndY), north, west) :- StartX > EndX.
directions(position(StartX, StartY), position(EndX, EndY), south, east) :- StartX < EndX & StartY < EndY.
directions(position(StartX, StartY), position(EndX, EndY), south, west) :- StartX > EndX.
directions(position(StartX, StartY), position(EndX, EndY), east, north) :- StartX < EndX.
directions(position(StartX, StartY), position(EndX, EndY), west, north).

// opposite_direction(+Direction, -OppositeDirection)
opposite_direction(est, ovest).
opposite_direction(ovest, est).
opposite_direction(nord, sud).
opposite_direction(sud, nord).


/* Initial goals */

// !purchase.
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

+!handle_purchase_response(closest_shopping_facility(_, F, Facility_Position)) : current_city(C) <-
    !handle_purchase(F, Facility_Position).

+!handle_purchase(F, Facility_Position) : position(X, Y) & current_city(C) &
        same_position(position(X, Y), Facility_Position) <-
    .print("User reached shopping facility ", F, " at position ", Facility_Position);
    .my_name(User);
    .send(C, achieve, make_purchase(User, F, 5)).

-!handle_purchase(F, Facility_Position) : position(X, Y) <-
    !move_towards_facility(Facility_Position);
    !handle_purchase(F, Facility_Position).

/* Plans for making a reservation at a dining facility */

+!make_reservation : current_city(C) & position(X, Y) <-
    .print("User is looking for a dining facility");
    .send(C, askOne, closest_dining_facility(position(X, Y), F, Facility_Position), Answer, 2000);
    !handle_reservation_response(Answer).

+!handle_reservation_response(closest_dining_facility(_, F, Facility_Position)) : current_city(C) <-
    !handle_reservation(F, Facility_Position).

+!handle_reservation(F, Facility_Position) : position(X, Y) & current_city(C) &
        same_position(position(X, Y), Facility_Position) <-
    .print("User reached dining facility ", F, " at position ", Facility_Position);
    .my_name(User);
    .send(C, achieve, make_reservation(User, F)).

-!handle_reservation(F, Facility_Position) : position(X, Y) <-
    !move_towards_facility(Facility_Position);
    !handle_reservation(F, Facility_Position).

/* Plans for moving towards a facility */

+!move_towards_facility(Facility_Position) : position(X, Y) & same_position(position(X, Y), Facility_Position) <-
    true.

+!move_towards_facility(Facility_Position) : position(X, Y) <-
    .print("User is moving towards facility: ", Facility_Position);
    ?directions(position(X, Y), Facility_Position, PrimaryDirection, SecondaryDirection);
    !try_direction(Facility_Position, PrimaryDirection, SecondaryDirection).

+!try_direction(Destination, PrimaryDirection, SecondaryDirection) : position(X, Y) <-
    .print("Trying direction: ", PrimaryDirection);
    move(PrimaryDirection);
    !move_towards_facility(Destination).

-!try_direction(Destination, PrimaryDirection, SecondaryDirection) : position(X, Y) <-
    .print("Failed to move in direction: ", PrimaryDirection, "; so trying direction: ", SecondaryDirection);
    move(SecondaryDirection);
    !move_towards_facility(Destination).

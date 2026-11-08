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

random_between(X, Y, math.floor(R *  (Y - X + 1) + X)) :- .random(R).

retries_left(5).

/* Initial goals */

// !purchase.
!make_reservation.


/* Percepts */

+position(X, Y) : not home(_) <-
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
    ?random_between(1, 10, PurchaseQuantity);
    .print("User wants to purchase ", PurchaseQuantity, " items");
    !handle_purchase_response(Answer, PurchaseQuantity).

+!handle_purchase_response(closest_shopping_facility(_, F, Facility_Position), PurchaseQuantity) : current_city(C) <-
    !handle_purchase(F, Facility_Position, PurchaseQuantity).

+!purchase_failed(F, PurchaseQuantity) : retries_left(Retries) & Retries > 0 <-
    .wait(500);
    .print("Retrying purchase. Retries left: ", Retries);
    -+retries_left(Retries - 1);
    !handle_purchase(F, PurchaseQuantity).

+!purchase_failed(F, PurchaseQuantity) : retries_left(Retries) & Retries == 0 <-
    .print("Failed to purchase ", PurchaseQuantity, " items after the maximum number of retries. Aborting purchase process.").

+!handle_purchase(F, PurchaseQuantity) : position(X, Y) & current_city(C) <-
    .my_name(User);
    .send(C, achieve, make_purchase(F, PurchaseQuantity)).

+!handle_purchase(F, Facility_Position, PurchaseQuantity) : position(X, Y) & current_city(C) &
        same_position(position(X, Y), Facility_Position) <-
    .print("User reached shopping facility ", F, " at position ", Facility_Position);
    .my_name(User);
    .send(C, achieve, make_purchase(F, PurchaseQuantity)).

-!handle_purchase(F, Facility_Position, PurchaseQuantity) : position(X, Y) <-
    !move_towards_facility(Facility_Position);
    !handle_purchase(F, Facility_Position, PurchaseQuantity).

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
    .send(C, achieve, make_reservation(F)).

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

+!try_direction(position(DestX, DestY), PrimaryDirection, SecondaryDirection) : position(X, Y) <-
    .print("Trying direction: ", PrimaryDirection);
    move(PrimaryDirection, DestX, DestY);
    !move_towards_facility(position(DestX, DestY)).

-!try_direction(position(DestX, DestY), PrimaryDirection, SecondaryDirection) : position(X, Y) <-
    .print("Failed to move in direction: ", PrimaryDirection, "; so trying direction: ", SecondaryDirection);
    !avoid_obstacle(position(DestX, DestY), PrimaryDirection, SecondaryDirection).

+!avoid_obstacle(position(DestX, DestY), PrimaryDirection, SecondaryDirection) : position(X, Y) <-
    .print("Avoiding obstacle while moving towards: ", position(DestX, DestY), " from position: ", position(X, Y));
    move(SecondaryDirection, DX, DY);
    !move_towards_facility(position(DestX, DestY)).

-!avoid_obstacle(position(DestX, DestY), PrimaryDirection, SecondaryDirection) : position(X, Y) <-
    .print("Failed to avoid obstacle in direction: ", PrimaryDirection, "; so trying direction: ", SecondaryDirection);
    opposite_direction(PrimaryDirection, OppositePrimaryDirection);
    opposite_direction(SecondaryDirection, OppositeSecondaryDirection);
    !try_direction(position(DestX, DestY), OppositePrimaryDirection, OppositeSecondaryDirection).

/* Beliefs and rules */

shopping_facility(s1).
shopping_facility(s2).
dining_facility(d1).
dining_facility(d2).
user(u1).

/* Initial goals */

!collect_facilities_info.

/* Plans */

+!collect_facilities_info <-
    .print("Collecting facilities.");
    .findall(SF, shopping_facility(SF), SFS);
    .findall(DF, dining_facility(DF), DFS);
    .concat(SFS, DFS, FS);
    !collect_facilities_info(FS).

+!collect_facilities_info([]).

+!collect_facilities_info([F | FS]) <-
    !!request_facility_info(F);
    !collect_facilities_info(FS).

+!request_facility_info(F) <-
    .print("Requesting information from facility: ", F);
    if (shopping_facility(F)) {
        .send(F, askOne, shopping_facility_info(Position, Q), Answer, 2000);
    } else {
        .send(F, askOne, dining_facility_info(Position, C), Answer, 2000);
    };
    !handle_answer(F, Answer).

+!handle_answer(F, Answer) <-
    .print("Received information from facility ", F, ": ", Answer);
    if (shopping_facility(F)) {
        -+facility_status(F, Position, Q);
        !!make_purchase(u1, F, 5);
    } else {
        -+facility_status(F, Position, C);
        !!make_reservation(u1, F);
    }.

-!handle_answer(F, Answer) <-
    .wait(500);
    .print("Retrying request for facility information.");
    !request_facility_info(F).

+!make_purchase(U, F, Quantity) : user(U) & shopping_facility(F) & facility_status(F, _, _) <-
    .print("Making a purchase of ", Quantity, " items from shopping facility ", F);
    .send(F, achieve, purchase(U, Quantity)).

-!make_purchase(U, F, Quantity) : user(U) & shopping_facility(F) <-
    .print("Cannot make a purchase from shopping facility ", F);
    !request_facility_info(F).

+!make_reservation(U, F) : user(U) & dining_facility(F) <-
    .print("Making a reservation for user ", U);
    .send(F, achieve, make_reservation(U)).

-!make_reservation(U, F) : user(U) & dining_facility(F) <-
    .print("Cannot make a reservation at dining facility ", F);
    !request_facility_info(F).

+!update_facility_status(Value)[source(F)] : (shopping_facility(F) | dining_facility(F)) & facility_status(F, _, _) <-
    -+facility_status(F, Position, Value);
    .print("Updating facility status for ", F, ": New Value = ", Value).

-!update_facility_status(Value)[source(F)] : (shopping_facility(F) | dining_facility(F)) <-
    .wait(500);
    .print("Impossible to update status. So asking for information.");
    !request_facility_info(F).

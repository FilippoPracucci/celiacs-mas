/* Plans */
!collect_shopping_facilities_info.
!collect_dining_facilities_info.

+!collect_shopping_facilities_info <-
    .print("Collecting shopping facilities.").
    .findall(F, shopping_facility(F), SF).

+!collect_shopping_facilities_info([F | SF]) <-
    !!request_shopping_facility_info(F);
    !collect_shopping_facilities_info(SF).

+!request_shopping_facility_info(F) : shopping_facility(F) <-
    .print("Requesting information from shopping facility: ", F);
    .send(F, askOne, shopping_facility_info(Position(X, Y), Q), Answer, 2000).

+!collect_dining_facilities_info <-
    .print("Collecting dining facilities.").
    .findall(F, dining_facility(F), DF).

+!collect_dining_facilities_info([F | DF]) <-
    !!request_dining_facility_info(F);
    !collect_dining_facilities_info(DF).

+!request_dining_facility_info(F) : dining_facility(F) <-
    .print("Requesting information from dining facility: ", F);
    .send(F, askOne, dining_facility_info(Position(X, Y), Q), Answer, 2000).

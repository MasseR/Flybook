package hlrv.flybook.db;

public final class DBConstants {

    // @formatter:off

    public final static String FILENAME = "flybook.db";

    public final static String TBLPREFIX = "";
    public final static String COLPREFIX = "";

    public final static String TABLE_USERS                              = TBLPREFIX + "Users";
    public final static String TABLE_FLIGHTENTRIES                      = TBLPREFIX + "FlightEntries";
    public final static String TABLE_AIRPORTS                           = TBLPREFIX + "Airports";
    public final static String TABLE_AIRCRAFTS                          = TBLPREFIX + "Aircrafts";

    public final static String USERS_USERNAME                           = COLPREFIX + "username";
    public final static String USERS_PASSWD                             = COLPREFIX + "passwd";
    public final static String USERS_FIRSTNAME                          = COLPREFIX + "firstname";
    public final static String USERS_LASTNAME                           = COLPREFIX + "lastname";
    public final static String USERS_ROLE                               = COLPREFIX + "role";
    public final static String USERS_EMAIL                              = COLPREFIX + "email";
    public final static String USERS_OPTLOCK                            = COLPREFIX + "optlock";

    public final static String FLIGHTENTRIES_FLIGHT_ID                  = COLPREFIX + "flight_id";
    public final static String FLIGHTENTRIES_USERNAME                   = COLPREFIX + "username";
    public final static String FLIGHTENTRIES_DATE                       = COLPREFIX + "date";
    public final static String FLIGHTENTRIES_AIRCRAFT                   = COLPREFIX + "aircraft";
    public final static String FLIGHTENTRIES_DEPARTURE_TIME             = COLPREFIX + "departure_time";
    public final static String FLIGHTENTRIES_DEPARTURE_AIRPORT          = COLPREFIX + "departure_airport";
    public final static String FLIGHTENTRIES_LANDING_TIME               = COLPREFIX + "landing_time";
    public final static String FLIGHTENTRIES_LANDING_AIRPORT            = COLPREFIX + "landing_airport";
    public final static String FLIGHTENTRIES_ONBLOCK_TIME               = COLPREFIX + "onblock_time";
    public final static String FLIGHTENTRIES_OFFBLOCK_TIME              = COLPREFIX + "offblock_time";
    public final static String FLIGHTENTRIES_FLIGHT_TYPE                = COLPREFIX + "flight_type";
    public final static String FLIGHTENTRIES_IFR_TIME                   = COLPREFIX + "ifr_time";
    public final static String FLIGHTENTRIES_NOTES                      = COLPREFIX + "notes";
    public final static String FLIGHTENTRIES_OPTLOCK                    = COLPREFIX + "optlock";
    public final static String FLIGHTENTRIES_PILOT_FULLNAME             = COLPREFIX + "pilot_fullname";
    public final static String FLIGHTENTRIES_FLIGHT_TIME                = COLPREFIX + "flight_time";
    public final static String FLIGHTENTRIES_AIRCRAFT_STRING            = COLPREFIX + "aircraft_string";
    public final static String FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING   = COLPREFIX + "departure_airport_string";
    public final static String FLIGHTENTRIES_LANDING_AIRPORT_STRING     = COLPREFIX + "landing_airport_string";

    public final static String AIRPORTS_ID                              = COLPREFIX + "id";
    public final static String AIRPORTS_CODE                            = COLPREFIX + "code";
    public final static String AIRPORTS_COUNTRY                         = COLPREFIX + "country";
    public final static String AIRPORTS_CITY                            = COLPREFIX + "city";
    public final static String AIRPORTS_NAME                            = COLPREFIX + "name";
    public final static String AIRPORTS_LOCATION                        = COLPREFIX + "location";
    public final static String AIRPORTS_OPTLOCK                         = COLPREFIX + "optlock";

    public final static String AIRCRAFTS_REGISTER                       = COLPREFIX + "register";
    public final static String AIRCRAFTS_USERNAME                       = COLPREFIX + "username";
    public final static String AIRCRAFTS_MAKE_MODEL                     = COLPREFIX + "make_model";
    public final static String AIRCRAFTS_ENGINE_COUNT                   = COLPREFIX + "engine_count";
    public final static String AIRCRAFTS_YEAR                           = COLPREFIX + "year";
    public final static String AIRCRAFTS_MAX_WEIGHT                     = COLPREFIX + "max_weight";
    public final static String AIRCRAFTS_CAPACITY                       = COLPREFIX + "capacity";
    public final static String AIRCRAFTS_OWNER                          = COLPREFIX + "owner";
    public final static String AIRCRAFTS_ADDRESS                        = COLPREFIX + "address";
    public final static String AIRCRAFTS_OPTLOCK                        = COLPREFIX + "optlock";

    // @formatter:on

}

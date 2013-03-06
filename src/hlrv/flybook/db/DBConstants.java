package hlrv.flybook.db;

public final class DBConstants {

    // @formatter:off

    public final static String FILENAME = "flybook.db";

    public final static String TABLE_USERS                            = "Users";
    public final static String TABLE_FLIGHTENTRIES                    = "FlightEntries";
    public final static String TABLE_AIRPORTS                         = "Airports";
    public final static String TABLE_AIRCRAFTS                        = "Aircrafts";

    public final static String USERS_USERNAME                         = "username";
    public final static String USERS_PASSWORD                         = "password";
    public final static String USERS_FIRSTNAME                        = "firstname";
    public final static String USERS_LASTNAME                         = "lastname";
    public final static String USERS_ROLE                             = "role";
    public final static String USERS_EMAIL                            = "email";
    public final static String USERS_OPTLOCK                          = "optlock";

    public final static String FLIGHTENTRIES_FLIGHT_ID                = "flight_id";
    public final static String FLIGHTENTRIES_USERNAME                 = "username";
    public final static String FLIGHTENTRIES_DATE                     = "date";
    public final static String FLIGHTENTRIES_AIRCRAFT                 = "aircraft";
    public final static String FLIGHTENTRIES_DEPARTURE_TIME           = "departure_time";
    public final static String FLIGHTENTRIES_DEPARTURE_AIRPORT        = "departure_airport";
    public final static String FLIGHTENTRIES_LANDING_TIME             = "landing_time";
    public final static String FLIGHTENTRIES_LANDING_AIRPORT          = "landing_airport";
    public final static String FLIGHTENTRIES_ONBLOCK_TIME             = "onblock_time";
    public final static String FLIGHTENTRIES_OFFBLOCK_TIME            = "offblock_time";
    public final static String FLIGHTENTRIES_FLIGHT_TYPE              = "flight_type";
    public final static String FLIGHTENTRIES_IFR_TIME                 = "ifr_time";
    public final static String FLIGHTENTRIES_NOTES                    = "notes";
    public final static String FLIGHTENTRIES_OPTLOCK                  = "optlock";
    public final static String FLIGHTENTRIES_PILOT_FULLNAME           = "pilot_fullname";
    public final static String FLIGHTENTRIES_FLIGHT_TIME              = "flight_time";
    public final static String FLIGHTENTRIES_AIRCRAFT_STRING          = "aircraft_string";
    public final static String FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING = "departure_airport_string";
    public final static String FLIGHTENTRIES_LANDING_AIRPORT_STRING   = "landing_airport_string";

    public final static String AIRPORTS_ID                            = "id";
    public final static String AIRPORTS_ICAO                          = "icao";
    public final static String AIRPORTS_IATA                          = "iata";
    public final static String AIRPORTS_COUNTRY                       = "country";
    public final static String AIRPORTS_CITY                          = "city";
    public final static String AIRPORTS_NAME                          = "name";
    public final static String AIRPORTS_LOCATION                      = "location";
    public final static String AIRPORTS_LATITUDE                      = "latitude";
    public final static String AIRPORTS_LONGITUDE                     = "longitude";
    public final static String AIRPORTS_OPTLOCK                       = "optlock";

    public final static String AIRCRAFTS_REGISTER                     = "register";
    public final static String AIRCRAFTS_USERNAME                     = "username";
    public final static String AIRCRAFTS_MAKE_MODEL                   = "make_model";
    public final static String AIRCRAFTS_ENGINE_COUNT                 = "engine_count";
    public final static String AIRCRAFTS_YEAR                         = "year";
    public final static String AIRCRAFTS_MAX_WEIGHT                   = "max_weight";
    public final static String AIRCRAFTS_CAPACITY                     = "capacity";
    public final static String AIRCRAFTS_OWNER                        = "owner";
    public final static String AIRCRAFTS_ADDRESS                      = "address";
    public final static String AIRCRAFTS_OPTLOCK                      = "optlock";

    // @formatter:on

}

package hlrv.flybook;

public final class DBConstants {

    // @formatter:off

    public final static String FILENAME = "flybook.db";

    public final static String TBLPREFIX = "";
    public final static String COLPREFIX = "c_";

    public final static String TABLE_USERS                              = TBLPREFIX + "Users";
    public final static String TABLE_FLIGHTENTRIES                      = TBLPREFIX + "FlightEntries";
    public final static String TABLE_AIRPORTS                           = TBLPREFIX + "Airports";
    public final static String TABLE_AIRCRAFTS                          = TBLPREFIX + "Aircrafts";

    public final static String COLUMN_USERS_USERNAME                    = COLPREFIX + "username";
    public final static String COLUMN_USERS_PASSWD                      = COLPREFIX + "passwd";
    public final static String COLUMN_USERS_FIRSTNAME                   = COLPREFIX + "firstname";
    public final static String COLUMN_USERS_LASTNAME                    = COLPREFIX + "lastname";
    public final static String COLUMN_USERS_ROLE                        = COLPREFIX + "role";
    public final static String COLUMN_USERS_EMAIL                       = COLPREFIX + "email";

    public final static String COLUMN_FLIGHTENTRIES_FLIGHT_ID           = COLPREFIX + "flight_id";
    public final static String COLUMN_FLIGHTENTRIES_USERNAME            = COLPREFIX + "username";
    public final static String COLUMN_FLIGHTENTRIES_DATE                = COLPREFIX + "date";
    public final static String COLUMN_FLIGHTENTRIES_AIRCRAFT            = COLPREFIX + "aircraft";
    public final static String COLUMN_FLIGHTENTRIES_DEPARTURE_TIME      = COLPREFIX + "departure_time";
    public final static String COLUMN_FLIGHTENTRIES_DEPARTURE_AIRPORT   = COLPREFIX + "departure_airport";
    public final static String COLUMN_FLIGHTENTRIES_LANDING_TIME        = COLPREFIX + "landing_time";
    public final static String COLUMN_FLIGHTENTRIES_LANDING_AIRPORT     = COLPREFIX + "landing_airport";
    public final static String COLUMN_FLIGHTENTRIES_ONBLOCK_TIME        = COLPREFIX + "onblock_time";
    public final static String COLUMN_FLIGHTENTRIES_OFFBLOCK_TIME       = COLPREFIX + "offblock_time";
    public final static String COLUMN_FLIGHTENTRIES_FLIGHT_TYPE         = COLPREFIX + "flight_type";
    public final static String COLUMN_FLIGHTENTRIES_IFR_TIME            = COLPREFIX + "ifr_time";
    public final static String COLUMN_FLIGHTENTRIES_NOTES               = COLPREFIX + "notes";
    public final static String COLUMN_FLIGHTENTRIES_OPTLOCK             = COLPREFIX + "optlock";

    public final static String COLUMN_AIRPORTS_ID                       = COLPREFIX + "id";
    public final static String COLUMN_AIRPORTS_CODE                     = COLPREFIX + "code";
    public final static String COLUMN_AIRPORTS_COUNTRY                  = COLPREFIX + "country";
    public final static String COLUMN_AIRPORTS_CITY                     = COLPREFIX + "city";
    public final static String COLUMN_AIRPORTS_NAME                     = COLPREFIX + "name";
    public final static String COLUMN_AIRPORTS_LOCATION                 = COLPREFIX + "location";

    public final static String COLUMN_AIRCRAFTS_REGISTER                = COLPREFIX + "register";
    public final static String COLUMN_AIRCRAFTS_CLASS                   = COLPREFIX + "class";
    public final static String COLUMN_AIRCRAFTS_CAPACITY                = COLPREFIX + "capacity";
    public final static String COLUMN_AIRCRAFTS_WEIGHT                  = COLPREFIX + "weight";

    // @formatter:on

}


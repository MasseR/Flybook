package hlrv.flybook;

public enum FlightType {
    /**
     * NOTE: Make sure order ordinals have no holes!
     */

    UNDEFINED("Undefined"), HOBBY("Hobby"), COMMERCIAL("Commercial"), TRAINING(
            "Training"), SEARCH_AND_RESCUE("Search and Rescue");
    // UNDEFINED("Undefined"), DOMESTIC("Domestic"), HOBBY("Hobby"),
    // TRANSREGIONAL(
    // "Transregional"), TRANSCONTINENTAL("Transcontinental");

    /**
     * Ugly, but somehow we need to convert Int -> Enum.
     */
    private static FlightType[] values = FlightType.values();

    private String name;

    private FlightType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FlightType toEnum(int i) {
        return values[i];
    }

    @Override
    public String toString() {
        return name;
    }

}

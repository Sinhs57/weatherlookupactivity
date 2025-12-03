package comexampleproject.model;
/*
  Author: Shakib Sinha
  Date: April 30th, 2025
  Subject: IT-634-10524-M01 Distributed App Development 2025
  Instructor: Professor Ace Gayhart
  Assignment: 6-1 Activity: Write a Client Program to Check the Weather
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a weather forecast with metadata and detailed forecast periods.
 */
public class Forecast implements Serializable {

    private static final long serialVersionUID = 1L;

    // Forecast title (e.g., timestamp or summary)
    private String title;

    // City name (e.g., "Boston")
    private String city;

    // State abbreviation or name (e.g., "MA" or "Massachusetts")
    private String state;

    // List of forecast periods like "Tonight", "Saturday", etc.
    private List<ForecastPeriod> periods = new ArrayList<>();

    /** Default constructor. Required for deserialization or frameworks. */
    public Forecast() {}

    /**
     * Gets the title of the forecast (usually the last updated timestamp).
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the forecast title.
     * @param title the updated timestamp or a label like "Forecast"
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    /** Gets the city name associated with the forecast. */
    public String getCity() {
        return city;
    }

    /** Sets the city name for the forecast. */
    public void setCity(String city) {
        this.city = city;
    }

    /** Gets the state name or abbreviation. */
    public String getState() {
        return state;
    }

    /** Sets the state name or abbreviation. */
    public void setState(String state) {
        this.state = state;
    }

    /** Returns the list of forecast periods. */
    public List<ForecastPeriod> getPeriods() {
        return periods;
    }

    /**
     * Adds a forecast period with a name and details.
     * @param name label for the period (e.g., "Tonight")
     * @param details full weather description
     */
    public void addPeriod(String name, String details) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (details == null || details.trim().isEmpty()) {
            throw new IllegalArgumentException("Details cannot be null or empty.");
        }
        periods.add(new ForecastPeriod(name, details));
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", periods=" + periods +
                '}';
    }

    /**
     * Represents a single time-based forecast entry.
     */
    public static class ForecastPeriod implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String name;    // e.g., "Monday Night"
        private final String details; // e.g., "Partly cloudy, low near 40F"

        /**
         * Constructor for a forecast period.
         */
        public ForecastPeriod(String name, String details) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty.");
            }
            if (details == null || details.trim().isEmpty()) {
                throw new IllegalArgumentException("Details cannot be null or empty.");
            }
            this.name = name;
            this.details = details;
        }

        public String getName() {
            return name;
        }

        public String getDetails() {
            return details;
        }

        @Override
        public String toString() {
            return "ForecastPeriod{" +
                    "name='" + name + '\'' +
                    ", details='" + details + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ForecastPeriod that = (ForecastPeriod) o;
            return Objects.equals(name, that.name) && Objects.equals(details, that.details);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, details);
        }
    }
}

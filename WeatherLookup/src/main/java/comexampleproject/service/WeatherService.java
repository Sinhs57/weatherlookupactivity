package comexampleproject.service;
/*
  Author: Shakib Sinha
  Date: April 30th, 2025
  Subject: IT-634-10524-M01 Distributed App Development 2025
  Instructor: Professor Ace Gayhart
  Assignment: 6-1 Activity: Write a Client Program to Check the Weather
*/
import comexampleproject.model.Forecast;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
public class WeatherService {

    // Logger for tracking events, warnings, and errors
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    // Limit how many forecast periods to return
    private static final int MAX_FORECAST_PERIODS = 5;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Constructor-based dependency injection
    public WeatherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Retrieves a weather forecast for a specific latitude and longitude.
     *
     * @param lat The latitude (string)
     * @param lon The longitude (string)
     * @return A populated Forecast object, or null if the API call fails or is invalid
     */
    public Forecast getForecast(String lat, String lon) {
        try {
            // Input validation
            if (!isValidLatitude(lat) || !isValidLongitude(lon)) {
                logger.error("Invalid coordinates: lat={}, lon={}", lat, lon);
                return null;
            }

            // Step 1: Get metadata (including forecast URL) from NWS Points API
            String pointsUrl = "https://api.weather.gov/points/" + lat + "," + lon;
            logger.debug("Calling Points API: {}", pointsUrl);
            String pointsResponse = restTemplate.getForObject(pointsUrl, String.class);
            JsonNode properties = objectMapper.readTree(pointsResponse).path("properties");

            // Validate if forecast URL is returned
            if (!properties.has("forecast")) {
                logger.warn("No forecast URL found for given coordinates.");
                return null;
            }

            // Extract city and state info from response
            String city = properties.path("relativeLocation").path("properties").path("city").asText("");
            String state = properties.path("relativeLocation").path("properties").path("state").asText("");

            // Step 2: Use the forecast URL to get the detailed weather data
            String forecastUrl = properties.get("forecast").asText();
            logger.debug("Calling Forecast API: {}", forecastUrl);
            String forecastResponse = restTemplate.getForObject(forecastUrl, String.class);
            JsonNode forecastJson = objectMapper.readTree(forecastResponse).path("properties");
            JsonNode periods = forecastJson.path("periods");

            if (!periods.isArray() || periods.isEmpty()) {
                logger.warn("No periods data available.");
                return null;
            }

            // Step 3: Build the Forecast object
            Forecast forecast = new Forecast();

            // Use timestamp if present, otherwise fallback title
            String updated = forecastJson.path("updated").asText(null);
            forecast.setTitle(updated != null && !updated.trim().isEmpty() ? updated : "Forecast");

            // Set extracted city and state
            forecast.setCity(city);
            forecast.setState(state);

            // Add forecast periods (up to MAX_FORECAST_PERIODS)
            for (int i = 0; i < Math.min(periods.size(), MAX_FORECAST_PERIODS); i++) {
                JsonNode period = periods.get(i);
                forecast.addPeriod(
                    period.path("name").asText("N/A"),
                    period.path("detailedForecast").asText("No details available.")
                );
            }

            return forecast;

        } catch (HttpClientErrorException e) {
            logger.error("HTTP error while fetching forecast: {}", e.getMessage());
        } catch (ResourceAccessException e) {
            logger.error("Network error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }

        // If we reach here, something went wrong
        return null;
    }

    /**
     * Validates latitude input is numeric and within -90 to 90.
     */
    public boolean isValidLatitude(String lat) {
        try {
            double latitude = Double.parseDouble(lat);
            return latitude >= -90 && latitude <= 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates longitude input is numeric and within -180 to 180.
     */
    public boolean isValidLongitude(String lon) {
        try {
            double longitude = Double.parseDouble(lon);
            return longitude >= -180 && longitude <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

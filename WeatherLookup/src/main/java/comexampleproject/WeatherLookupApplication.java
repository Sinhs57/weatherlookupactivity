package comexampleproject;

/*
  Author: Shakib Sinha
  Date: April 30th, 2025
  Subject: IT-634-10524-M01 Distributed App Development 2025
  Instructor: Professor Ace Gayhart
  Assignment: 6-1 Activity: Write a Client Program to Check the Weather
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Entry point for the Weather Lookup Spring Boot application.
 * Initializes and runs the Spring application context.
 */
@SpringBootApplication
public class WeatherLookupApplication {

    // Logger for tracking application startup status
    private static final Logger logger = LoggerFactory.getLogger(WeatherLookupApplication.class);

    /**
     * Main method that launches the Spring Boot application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Start the Spring Boot application
            Environment environment = SpringApplication.run(WeatherLookupApplication.class, args).getEnvironment();

            // Log application startup details
            logger.info("WeatherLookupApplication has started successfully.");
            logger.info("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));
            logger.info("Application Name: {}", environment.getProperty("spring.application.name", "Weather Lookup Application"));
            logger.info("Application Version: {}", environment.getProperty("application.version", "1.0.0"));

        } catch (Exception e) {
            logger.error("Unexpected error while retrieving forecast", e);
        }

    }
}
package comexampleproject.controller;

/*
  Author: Shakib Sinha
  Date: April 30th, 2025
  Subject: IT-634-10524-M01 Distributed App Development 2025
  Instructor: Professor Ace Gayhart
  Assignment: 6-1 Activity: Write a Client Program to Check the Weather
*/

import comexampleproject.model.Forecast;
import comexampleproject.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class responsible for handling user requests for weather forecasts.
 */
@Controller
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    // Error messages
    private static final String ERROR_INVALID_COORDINATES =
        "Invalid latitude or longitude. Please enter valid coordinates (latitude: -90 to 90, longitude: -180 to 180).";
    private static final String ERROR_NO_FORECAST =
        "Weather data not available for the given location.";
    private static final String ERROR_UNEXPECTED =
        "An unexpected error occurred. Please try again later.";

    // View templates
    private static final String VIEW_INDEX = "index";
    private static final String VIEW_RESULTS = "results";

    @Autowired
    private WeatherService weatherService;

    /**
     * Handles GET requests to the root URL ("/") and displays the weather lookup form.
     *
     * @return The name of the view template for the form.
     */
    @GetMapping("/")
    public String showForm() {
        logger.info("Accessed the root URL to display the form.");
        return VIEW_INDEX;
    }

    /**
     * Handles POST requests to "/getForecast" and fetches the weather forecast for the given coordinates.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     * @param model     The model to pass data to the view.
     * @return The name of the view template to display the results or errors.
     */
    @PostMapping("/getForecast")
    public String getForecast(@RequestParam String latitude,
                              @RequestParam String longitude,
                              Model model) {
        logger.info("Received request to fetch forecast for lat={}, lon={}", latitude, longitude);

        try {
            // Validate inputs
            if (!weatherService.isValidLatitude(latitude) || !weatherService.isValidLongitude(longitude)) {
                logger.warn("Invalid coordinates: lat={}, lon={}", latitude, longitude);
                model.addAttribute("error", ERROR_INVALID_COORDINATES);
                return VIEW_INDEX;
            }

            // Get forecast from service
            Forecast forecast = weatherService.getForecast(latitude, longitude);
            if (forecast == null) {
                logger.warn("No forecast returned for coordinates: lat={}, lon={}", latitude, longitude);
                model.addAttribute("error", ERROR_NO_FORECAST);
                return VIEW_INDEX;
            }

            // Display results
            model.addAttribute("forecast", forecast);
            return VIEW_RESULTS;

        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            model.addAttribute("error", ERROR_UNEXPECTED);
            return VIEW_INDEX;
        }
    }
}
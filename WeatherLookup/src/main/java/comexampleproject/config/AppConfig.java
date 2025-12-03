package comexampleproject.config;
/*
  Author: Shakib Sinha
  Date: April 30th, 2025
  Subject: IT-634-10524-M01 Distributed App Development 2025
  Instructor: Professor Ace Gayhart
  Assignment: 6-1 Activity: Write a Client Program to Check the Weather
*/
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Marks this class as a source of Spring bean definitions
public class AppConfig {

    /**
     * Defines a RestTemplate bean to be used throughout the application
     * for making HTTP requests to external APIs.
     * 
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

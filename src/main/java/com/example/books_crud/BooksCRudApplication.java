package com.example.books_crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main application entry point for the Literary Works Management System
 * This system provides functionality to manage literary works and their creators
 */
@SpringBootApplication
public class BooksCRudApplication {

    /**
     * Application bootstrap method
     * @param commandLineArgs Command line arguments passed to the application
     */
    public static void main(String[] commandLineArgs) {
        ConfigurableApplicationContext applicationContext = 
            SpringApplication.run(BooksCRudApplication.class, commandLineArgs);
        
        // Log successful startup
        System.out.println("Literary Works Management System started successfully");
    }
}

package com.jesussoto.sparklewebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.hp.hpl.jena.rdf.model.Model;
import com.jesussoto.sparklewebservice.rdf.RDFModel;

//Tell Spring to automatically inject any dependencies that are marked in
//our classes with @Autowired
@EnableAutoConfiguration
//Tell Spring that this object represents a Configuration for the
//application
@Configuration
//Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
//so that requests can be routed to our Controllers)
@EnableWebMvc
//Tell Spring to go and scan our controller package (and all sub packages) to
//find any Controllers or other components that are part of our applciation.
//Any class in this package that is annotated with @Controller is going to be
//automatically discovered and connected to the DispatcherServlet.
@ComponentScan
public class Application {

    // Launch the application.
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Bean for inject model dependencies throughout the application.
    @Bean
    public Model createDefaultModel() {
        return RDFModel.getInstance();
    }

}

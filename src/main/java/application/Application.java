package application;

import configuration.Configuration;
import org.springframework.boot.SpringApplication;

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Configuration.class, args);
    }

}

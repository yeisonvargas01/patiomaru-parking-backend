package co.edu.uco.patiomaruparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"co.edu.uco.patiomaruparking"})
public class PatiomaruParkingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatiomaruParkingBackendApplication.class, args);
    }
}

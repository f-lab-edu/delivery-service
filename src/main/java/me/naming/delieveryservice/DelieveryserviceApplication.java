package me.naming.delieveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class DelieveryserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelieveryserviceApplication.class, args);
    }

}

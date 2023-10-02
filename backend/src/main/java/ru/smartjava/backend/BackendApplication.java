package ru.smartjava.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.smartjava.backend.config.Const;

@SpringBootApplication
public class BackendApplication {

//    @Autowired
//    private Const aConst;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}

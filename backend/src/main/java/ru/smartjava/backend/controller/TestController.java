package ru.smartjava.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    //    @RolesAllowed({"UPLOAD"})
    @Secured({"DOWNLOAD"})
    @GetMapping("check")
    ResponseEntity<String> check() {
        return ResponseEntity.ok("Работает");
    }


}

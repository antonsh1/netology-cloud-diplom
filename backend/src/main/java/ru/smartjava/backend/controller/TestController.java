package ru.smartjava.backend.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {

//    @RolesAllowed({"UPLOAD"})
    @Secured({"DOWNLOAD"})
    @GetMapping("check")
    ResponseEntity<String> check() {
        return ResponseEntity.ok("Работает");
    }

}

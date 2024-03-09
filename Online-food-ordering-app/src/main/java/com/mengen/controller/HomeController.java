package com.mengen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to our online food ordering app");
    }
}

package com.sipc.hospitalalarmsystem.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@CrossOrigin
public class AlarmController {
    @PostMapping("/api/v1/alarm")
    public void alarm(@RequestParam int id,@RequestParam int caseType) {

    }
}

package com.funny.combo.codegen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by Joe on 2020-03-12.
 */
@RestController
public class HealthController {
    @GetMapping("/health/status")
    public String health() {
        return "ok";
    }

}

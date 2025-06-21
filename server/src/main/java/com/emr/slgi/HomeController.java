package com.emr.slgi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "test home";
    }

    @GetMapping("/unauth")
    public String unauth() {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/unauth2")
    public String unauth2() {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "test unauthorization");
    }

    @GetMapping("/notfound")
    public String notFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

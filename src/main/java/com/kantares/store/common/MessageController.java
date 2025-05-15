package com.kantares.store.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @GetMapping(value = "/hello")
    public Message sayHello() {
        return new Message("Hello, World!");
    }
}

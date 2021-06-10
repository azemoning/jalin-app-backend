package com.jalin.jalinappbackend.utility;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtility {
    public RestTemplate initialize() {
        return new RestTemplate();
    }
}

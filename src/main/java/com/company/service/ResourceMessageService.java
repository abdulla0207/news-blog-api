package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ResourceMessageService {
    @Autowired
    private ResourceBundleMessageSource messageSource;
    public String getMessage(String code, String lang) {
        return messageSource.getMessage(code, null, new Locale(lang));
    }
}

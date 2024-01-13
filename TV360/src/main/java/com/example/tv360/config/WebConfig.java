package com.example.tv360.config;

import com.example.tv360.utils.StringArrayToCastDTOSetConverter;
import com.example.tv360.utils.StringArrayToCategoryDTOSetConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public StringArrayToCategoryDTOSetConverter stringArrayToCategoryDTOSetConverter() {
        return new StringArrayToCategoryDTOSetConverter();
    }

    @Bean
    public StringArrayToCastDTOSetConverter stringArrayToCastDTOSetConverter() {
        return new StringArrayToCastDTOSetConverter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringArrayToCategoryDTOSetConverter());
        registry.addConverter(stringArrayToCastDTOSetConverter());
    }


}

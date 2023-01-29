package com.karmen.taskmanager.config;

import com.karmen.taskmanager.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MapperConfig {
    private final EmployeeService employeeService;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

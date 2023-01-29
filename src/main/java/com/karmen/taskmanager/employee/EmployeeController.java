package com.karmen.taskmanager.employee;

import com.karmen.taskmanager.task.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Collection<Employee>> getEmployees() {
        var result = employeeService.getAllEmployees();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        if (employeeId != null) {
            return employeeService.findById(employeeId)
                    .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity createNewEmployee(@RequestBody Employee employee) {
        if (employee.getId() != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        var result = employeeService.save(employee);
        var headers = new HttpHeaders();
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(result, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateEmployee(@RequestBody Employee employee) {
        var currentTask = employeeService.findById(employee.getId());
        if(currentTask.isPresent()) {
            employeeService.save(employee);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity deleteTask(@PathVariable Long employeeId) {
        employeeService.deleteById(employeeId);
        return new ResponseEntity(HttpStatus.OK);
    }
}

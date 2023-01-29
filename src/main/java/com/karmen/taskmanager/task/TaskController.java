package com.karmen.taskmanager.task;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Collection<TaskDTO>> getTasks() {
        var result = taskService.getAllTasks().stream()
                .map(this::convertToDto)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        return taskService.findById(taskId)
                .map(value -> new ResponseEntity<>(convertToDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity createNewTask(@RequestBody TaskDTO taskDTO) {
        if (taskDTO.getId() != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        var result = taskService.save(convertToEntity(taskDTO));
        var headers = new HttpHeaders();
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(convertToDto(result), headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateTask(@RequestBody TaskDTO taskDTO) {
        var currentTask = taskService.findById(taskDTO.getId());
        if(currentTask.isPresent()) {
            taskService.save(convertToEntity(taskDTO));
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {
        taskService.deleteById(taskId);
        return new ResponseEntity(HttpStatus.OK);
    }

    private TaskDTO convertToDto(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    private Task convertToEntity(TaskDTO dto) {
        return modelMapper.map(dto, Task.class);
    }
}

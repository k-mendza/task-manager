package com.karmen.taskmanager.task;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Collection<Task> getAllTasks() {
        return IterableUtils.toList(taskRepository.findAll());
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task save(Task entity) {
        return taskRepository.save(entity);
    }

    public void deleteById(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}


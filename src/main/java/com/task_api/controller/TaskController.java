package com.task_api.controller;

import com.task_api.model.Task;
import com.task_api.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Task>createTask(@RequestBody @Valid TaskRequest request){
        Task task = service.createTask(request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> listAllTasks() {
        return ResponseEntity.ok(service.listAllTasks());
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task>completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(service.completeTask(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Task>> listPendingTasks() {
        return ResponseEntity.ok(service.listPendingTasks());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> listCompletedTasks() {
        return ResponseEntity.ok(service.listCompletedTasks());
    }

    public record TaskRequest(
            @NotBlank(message = "O titulo nao pode ser vazio.") String title,
            String description
    ){}
}

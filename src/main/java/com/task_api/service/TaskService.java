package com.task_api.service;

import com.task_api.model.Task;
import com.task_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    public Task createTask(String title, String description) {
        Task task = new Task(title, description);
        return repository.save(task);
    }

    public List<Task> listAllTasks() {
        return repository.findAll();
    }

    public Task completeTask(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa com id " + id + "nao encontrada"));

        task.complete();
        return repository.save(task);
    }

    public List<Task> listPendingTasks() {
        return repository.findByCompleted(false);
    }

    public List<Task> listCompletedTasks() {
        return repository.findByCompleted(true);
    }
}



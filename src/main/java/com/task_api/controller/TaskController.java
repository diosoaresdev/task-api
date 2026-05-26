package com.task_api.controller;

import com.task_api.model.Task;
import com.task_api.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name ="Tasks", description = "Gerenciamento de tarefas")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }


    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Task>createTask(@RequestBody @Valid TaskRequest request){
        Task task = service.createTask(request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Task>> listAllTasks() {
        return ResponseEntity.ok(service.listAllTasks());
    }

    @Operation(summary = "Concluir tarefa", description = "Marca uma tarefa como concluída")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa concluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task>completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(service.completeTask(id));
    }

    @Operation(summary = "Listar pendentes", description = "Retorna tarefas não concluídas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/pending")
    public ResponseEntity<List<Task>> listPendingTasks() {
        return ResponseEntity.ok(service.listPendingTasks());
    }

    @Operation(summary = "Listar concluídas", description = "Retorna tarefas concluídas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/completed")
    public ResponseEntity<List<Task>> listCompletedTasks() {
        return ResponseEntity.ok(service.listCompletedTasks());
    }

    public record TaskRequest(
            @NotBlank(message = "O titulo nao pode ser vazio.") String title,
            String description
    ){}
}
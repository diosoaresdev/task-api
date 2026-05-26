package com.task_api.controller;

import com.task_api.model.Task;
import com.task_api.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService service;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Estudar MockMvc", "Testes de controller");
    }

    @Test
    void deveCriarTarefaERetornar201() throws Exception {
        when(service.createTask(any(), any())).thenReturn(task);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TaskController.TaskRequest("Estudar MockMvc", "Testes de controller")
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Estudar MockMvc"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void deveListarTodasAsTarefasERetornar200() throws Exception {
        when(service.listAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Estudar MockMvc"));
    }

    @Test
    void deveConcluirTarefaERetornar200() throws Exception {
        task.complete();
        when(service.completeTask(eq(1L))).thenReturn(task);

        mockMvc.perform(patch("/tasks/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deveRetornar400QuandoTituloVazio() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TaskController.TaskRequest("", "Descrição")
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }
}
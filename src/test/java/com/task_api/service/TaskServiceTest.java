package com.task_api.service;

import com.task_api.model.Task;
import com.task_api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Estudar Mockito", "Testes com mocks");
    }

    @Test
    void deveCriarTarefaComSucesso() {
        when(repository.save(any(Task.class))).thenReturn(task);

        Task result = service.createTask("Estudar Mockito", "Testes com mocks");

        assertNotNull(result);
        assertEquals("Estudar Mockito", result.getTitle());
        assertFalse(result.isCompleted());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void deveListarTodasAsTarefas() {
        when(repository.findAll()).thenReturn(List.of(task));

        List<Task> result = service.listAllTasks();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveConcluirTarefaComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        Task result = service.completeTask(1L);

        assertTrue(result.isCompleted());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(task);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                service.completeTask(99L)
        );

        verify(repository, never()).save(any(Task.class));
    }

    @Test
    void deveListarTarefasPendentes() {
        when(repository.findByCompleted(false)).thenReturn(List.of(task));

        List<Task> result = service.listPendingTasks();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isCompleted());
        verify(repository, times(1)).findByCompleted(false);
    }
}
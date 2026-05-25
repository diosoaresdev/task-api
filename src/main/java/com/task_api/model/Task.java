package com.task_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O titulo nao pode ser vazio.")
    @Column(nullable = false)
    private String title;

    private String description;

    private boolean completed;

    private LocalDate createdAt;

    public Task() {}

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDate.now();
    }

    public void complete() {
        this.completed = true;
    }

    public Long getId() {return id;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public boolean isCompleted() {return completed;}
    public LocalDate getCreatedAt() {return createdAt;}
}

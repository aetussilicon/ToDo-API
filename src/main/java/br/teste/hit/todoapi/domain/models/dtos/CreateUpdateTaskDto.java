package br.teste.hit.todoapi.domain.models.dtos;

import br.teste.hit.todoapi.domain.models.enums.TaskPriorityLevel;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link br.teste.hit.todoapi.domain.models.entities.UserTask}
 */
public record CreateUpdateTaskDto(@NotBlank String title, String description, TaskPriorityLevel priorityLevel,
                                  TaskStatus status, LocalDateTime deadLine) implements Serializable {
}
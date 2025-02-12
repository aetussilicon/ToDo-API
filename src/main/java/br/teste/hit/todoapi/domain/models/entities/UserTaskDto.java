package br.teste.hit.todoapi.domain.models.entities;

import br.teste.hit.todoapi.domain.models.enums.TaskPriorityLevel;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DTO for {@link UserTask}
 */
public record UserTaskDto(Long taskId, String title, String description, TaskPriorityLevel priorityLevel,
                          TaskStatus status, Timestamp createdDate, Timestamp updatedDate,
                          LocalDateTime deadLine) implements Serializable {
}
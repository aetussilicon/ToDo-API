package br.teste.hit.todoapi.domain.ports.services;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import br.teste.hit.todoapi.domain.models.enums.TaskPriorityLevel;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;

import java.util.List;

public interface TasksServices {
    UserTaskDto createTask(CreateUpdateTaskDto createDto);
    UserTaskDto updateTask(String id, CreateUpdateTaskDto updateDto);
    UserTaskDto listTask(String id);
    List<UserTaskDto> listAllTasks();
    List<UserTaskDto> listAllTasksByStatus(TaskStatus status);
    List<UserTaskDto> listAllTasksByPriorityLevel(TaskPriorityLevel priorityLevel);
    void changeCompletionStatus(boolean completed);
    void deleteTask(String id);
}

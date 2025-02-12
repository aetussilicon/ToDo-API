package br.teste.hit.todoapi.application.services;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.entities.UserTask;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import br.teste.hit.todoapi.domain.models.enums.TaskPriorityLevel;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;
import br.teste.hit.todoapi.domain.ports.mappers.UserTaskMapper;
import br.teste.hit.todoapi.domain.ports.repositories.UserTaskRepository;
import br.teste.hit.todoapi.domain.ports.services.TasksServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TasksServicesImpl implements TasksServices {
    private final UserTaskRepository repository;
    private final UserTaskMapper userTaskMapper;

    @Override
    public UserTaskDto createTask(CreateUpdateTaskDto createDto) {
        UserTask task = userTaskMapper.toEntity(createDto);
        task = repository.save(task);
        return userTaskMapper.toDto(task);
    }

    @Override
    public UserTaskDto updateTask(String id, CreateUpdateTaskDto updateDto) {
        UserTask taskToUpdate = repository.findById(Long.valueOf(id)).orElseThrow(RuntimeException::new);
        taskToUpdate = userTaskMapper.partialUpdate(updateDto, taskToUpdate);
        taskToUpdate = repository.save(taskToUpdate);
        return userTaskMapper.toDto(taskToUpdate);
    }

    @Override
    public UserTaskDto listTask(String id) {
        return null;
    }

    @Override
    public List<UserTaskDto> listAllTasks() {
        return userTaskMapper.toDto(repository.findAll());
    }

    @Override
    public List<UserTaskDto> listAllTasksByStatus(TaskStatus status) {
        return List.of();
    }

    @Override
    public List<UserTaskDto> listAllTasksByPriorityLevel(TaskPriorityLevel priorityLevel) {
        return List.of();
    }

    @Override
    public void setTaskComplete(String id) {
        UserTask task = repository.findById(Long.valueOf(id)).orElseThrow(RuntimeException::new);
        task.setStatus(TaskStatus.DONE);
        repository.save(task);
    }

    @Override
    public void deleteTask(String id) {
        UserTask taskToDelete = repository.findById(Long.valueOf(id)).orElseThrow(RuntimeException::new);
        repository.deleteById(taskToDelete.getTaskId());
    }
}
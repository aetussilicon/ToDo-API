package br.teste.hit.todoapi.application.controllers;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.dtos.TaskStatisticDto;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import br.teste.hit.todoapi.domain.ports.controllers.TasksController;
import br.teste.hit.todoapi.domain.ports.services.TasksServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TasksControllerImpl implements TasksController {
    private final TasksServices services;

    @Override
    public ResponseEntity<UserTaskDto> createNewTask(CreateUpdateTaskDto createDto) {
        return new ResponseEntity<>(services.createTask(createDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserTaskDto> updateTask(String id, CreateUpdateTaskDto updateDto) {
        return new ResponseEntity<>(services.updateTask(id, updateDto), HttpStatus.OK);
    }

    @Override
    public HttpStatus deleteTask(String id) {
        services.deleteTask(id);
        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<List<UserTaskDto>> listTasks() {
        return new ResponseEntity<>(services.listAllTasks(), HttpStatus.OK);
    }

    @Override
    public HttpStatus completeTask(String id) {
        services.setTaskComplete(id);
        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<TaskStatisticDto> getStatistics() {
        return new ResponseEntity<>(services.getStatistics(), HttpStatus.OK);
    }
}

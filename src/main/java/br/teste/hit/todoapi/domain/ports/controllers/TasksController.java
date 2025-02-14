package br.teste.hit.todoapi.domain.ports.controllers;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.dtos.TaskStatisticDto;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TasksController {

    @PostMapping
    ResponseEntity<UserTaskDto> createNewTask(@Valid @RequestBody CreateUpdateTaskDto createDto);

    @PutMapping("{id}")
    ResponseEntity<UserTaskDto> updateTask(@PathVariable String id, @RequestBody CreateUpdateTaskDto updateDto);

    @DeleteMapping("{id}")
    HttpStatus deleteTask(@PathVariable String id);

    @GetMapping
    ResponseEntity<List<UserTaskDto>> listTasks();

    @PatchMapping("{id}/complete")
    HttpStatus completeTask(@PathVariable String id);

    @GetMapping("statistics")
    ResponseEntity<TaskStatisticDto> getStatistics();
}

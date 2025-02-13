package br.teste.hit.todoapi.application.services;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.entities.UserTask;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import br.teste.hit.todoapi.domain.models.enums.TaskPriorityLevel;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;
import br.teste.hit.todoapi.domain.ports.mappers.UserTaskMapper;
import br.teste.hit.todoapi.domain.ports.repositories.UserTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TasksServicesImplTest {

    @Mock
    private UserTaskRepository userTaskRepository;

    @Mock
    private UserTaskMapper userTaskMapper;

    @InjectMocks
    private TasksServicesImpl service;

    private UserTask task;
    private CreateUpdateTaskDto createUpdateDto;
    private UserTaskDto expectedResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task = new UserTask();
        task.setTaskId(1L);
        task.setTitle("test");
        task.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        task.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        expectedResponse = new UserTaskDto(
                1L, "test", null,
                null, null, task.getCreatedDate(), task.getUpdatedDate(), null
        );
    }

    @Test
    void createTask() {
        createUpdateDto = new CreateUpdateTaskDto(
                "test", null, null, null, null
        );
        
        when(userTaskMapper.toEntity(createUpdateDto)).thenReturn(task);
        when(userTaskRepository.save(task)).thenReturn(task);
        when(userTaskMapper.toDto(task)).thenReturn(expectedResponse);
        
        UserTaskDto request = service.createTask(createUpdateDto);

        verify(userTaskMapper, times(1)).toEntity(createUpdateDto);
        verify(userTaskRepository, times(1)).save(task);
        verify(userTaskMapper, times(1)).toDto(task);
        assertEquals(request, expectedResponse);
    }

    @Test
    void updateTask() {
        createUpdateDto = new CreateUpdateTaskDto(
                "test update", "test description", TaskPriorityLevel.LOW,
                TaskStatus.IN_PROGRESS, LocalDate.now()
        );

        UserTask updatedTask = new UserTask();
        updatedTask.setTaskId(task.getTaskId());
        updatedTask.setTitle(createUpdateDto.title());
        updatedTask.setDescription(createUpdateDto.description());
        updatedTask.setPriorityLevel(createUpdateDto.priorityLevel());
        updatedTask.setStatus(createUpdateDto.status());
        updatedTask.setDeadLine(createUpdateDto.deadLine());
        updatedTask.setCreatedDate(task.getCreatedDate());
        updatedTask.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        expectedResponse = new UserTaskDto(
                updatedTask.getTaskId(), updatedTask.getTitle(), updatedTask.getDescription(),
                updatedTask.getPriorityLevel(), updatedTask.getStatus(), updatedTask.getCreatedDate(),
                updatedTask.getUpdatedDate(), updatedTask.getDeadLine()
        );

        when(userTaskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userTaskMapper.partialUpdate(createUpdateDto, task)).thenReturn(updatedTask);
        when(userTaskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(userTaskMapper.toDto(updatedTask)).thenReturn(expectedResponse);

        UserTaskDto request = service.updateTask("1", createUpdateDto);

        verify(userTaskRepository, times(1)).findById(anyLong());
        verify(userTaskMapper, times(1)).partialUpdate(createUpdateDto, task);
        verify(userTaskRepository, times(1)).save(updatedTask);
        verify(userTaskMapper, times(1)).toDto(updatedTask);
        assertEquals(request, expectedResponse);
    }

    @Test
    void updateTask_TaskNotFound() {
        createUpdateDto = new CreateUpdateTaskDto(
                "test update", "test description", TaskPriorityLevel.LOW,
                TaskStatus.IN_PROGRESS, LocalDate.now()
        );

        when(userTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.updateTask("1", createUpdateDto);
        });

        verify(userTaskRepository, times(1)).findById(anyLong());
        verify(userTaskMapper, never()).partialUpdate(createUpdateDto, task);
        verify(userTaskRepository, never()).save(any(UserTask.class));
        verify(userTaskMapper, never()).toDto(any(UserTask.class));
    }

    @Test
    void listAllTasks() {
        List<UserTask> taskList = List.of(task);
        List<UserTaskDto> responseList = List.of(expectedResponse);

        when(userTaskRepository.findAll()).thenReturn(taskList);
        when(userTaskMapper.toDto(taskList)).thenReturn(responseList);

        List<UserTaskDto> request = service.listAllTasks();

        assertEquals(request, responseList);

        verify(userTaskRepository, times(1)).findAll();
        verify(userTaskMapper, times(1)).toDto(taskList);
    }

    @Test
    void setTaskComplete() {
        when(userTaskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        task.setStatus(TaskStatus.DONE);

        when(userTaskRepository.save(task)).thenReturn(task);

        service.setTaskComplete("1");

        verify(userTaskRepository, times(1)).findById(anyLong());
        verify(userTaskRepository, times(1)).save(task);
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void setTaskComplete_TaskNotFound() {
        when(userTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.setTaskComplete("1");
        });

        verify(userTaskRepository, times(1)).findById(anyLong());
        verify(userTaskRepository, never()).save(any(UserTask.class));
    }

    @Test
    void deleteTask() {
        when(userTaskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        service.deleteTask("1");

        verify(userTaskRepository, times(1)).findById(anyLong());
        verify(userTaskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteTask_TaskNotFound() {
        when(userTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.deleteTask("1");
        });

        verify(userTaskRepository, times(1)).findById(anyLong());
        verify(userTaskRepository, never()).deleteById(anyLong());
    }
}
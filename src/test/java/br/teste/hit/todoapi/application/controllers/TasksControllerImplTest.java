package br.teste.hit.todoapi.application.controllers;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import br.teste.hit.todoapi.domain.models.enums.TaskPriorityLevel;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;
import br.teste.hit.todoapi.domain.ports.repositories.UserTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TasksControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userTaskRepository.deleteAll();
    }

    @Test
    void createNewTask() throws Exception {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto(
                "Task Integration",
                "Integration Test",
                TaskPriorityLevel.MEDIUM,
                TaskStatus.PENDING,
                LocalDate.now().plusDays(1)
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Task Integration")));
    }


    @Test
    void updateTask() throws Exception {
        CreateUpdateTaskDto createDto = new CreateUpdateTaskDto(
                "Initial Task",
                "Initial Description",
                TaskPriorityLevel.MEDIUM,
                TaskStatus.PENDING,
                LocalDate.now().plusDays(1)
        );
        String createResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserTaskDto createdTask = objectMapper.readValue(createResponse, UserTaskDto.class);
        Long taskId = createdTask.taskId();

        CreateUpdateTaskDto updateDto = new CreateUpdateTaskDto(
                "Updated Task",
                "Updated Description",
                TaskPriorityLevel.HIGH,
                TaskStatus.IN_PROGRESS,
                LocalDate.now().plusDays(2)
        );
        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.priorityLevel", is("HIGH")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    void deleteTask() throws Exception {
        CreateUpdateTaskDto createDto = new CreateUpdateTaskDto(
                "Task To Delete",
                "Delete Description",
                TaskPriorityLevel.LOW,
                TaskStatus.PENDING,
                LocalDate.now().plusDays(1)
        );
        String createResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserTaskDto createdTask = objectMapper.readValue(createResponse, UserTaskDto.class);
        Long taskId = createdTask.taskId();

        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isOk());

        assertFalse(userTaskRepository.findById(taskId).isPresent());
    }

    @Test
    void listTasks() throws Exception {
        CreateUpdateTaskDto dto1 = new CreateUpdateTaskDto(
                "Task 1",
                "Desc 1",
                TaskPriorityLevel.MEDIUM,
                TaskStatus.PENDING,
                LocalDate.now().plusDays(1)
        );
        CreateUpdateTaskDto dto2 = new CreateUpdateTaskDto(
                "Task 2",
                "Desc 2",
                TaskPriorityLevel.HIGH,
                TaskStatus.DONE,
                LocalDate.now().plusDays(2)
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }

    @Test
    void completeTask() throws Exception {
        CreateUpdateTaskDto createDto = new CreateUpdateTaskDto(
                "Incomplete Task",
                "Description",
                TaskPriorityLevel.MEDIUM,
                TaskStatus.PENDING,
                LocalDate.now().plusDays(1)
        );
        String createResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserTaskDto createdTask = objectMapper.readValue(createResponse, UserTaskDto.class);
        Long taskId = createdTask.taskId();

        mockMvc.perform(patch("/tasks/" + taskId + "/complete"))
                .andExpect(status().isOk());

        // Verifica se o status foi atualizado para DONE
        var updatedTask = userTaskRepository.findById(taskId).orElseThrow();
        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
    }

    @Test
    void getStatistics() throws Exception {
        userTaskRepository.save(new br.teste.hit.todoapi.domain.models.entities.UserTask(null, "Task 1", null, null, TaskStatus.PENDING, null, null, null));
        userTaskRepository.save(new br.teste.hit.todoapi.domain.models.entities.UserTask(null, "Task 2", null, null, TaskStatus.DONE, null, null, null));

        mockMvc.perform(get("/tasks/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks", is(2)))
                .andExpect(jsonPath("$.completedTasks", is(1)))
                .andExpect(jsonPath("$.percentageCompleted", is(50.0)));

    }
}
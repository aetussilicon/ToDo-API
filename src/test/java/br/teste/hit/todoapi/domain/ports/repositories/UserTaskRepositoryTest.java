package br.teste.hit.todoapi.domain.ports.repositories;

import br.teste.hit.todoapi.domain.models.entities.UserTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTaskRepositoryTest {

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Test
    void SaveTask() {
        UserTask task = new UserTask();
        task.setTitle("test");

        UserTask savedTask = userTaskRepository.save(task);

        assertNotNull(savedTask.getTaskId());
    }
}
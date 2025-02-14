package br.teste.hit.todoapi.domain.ports.repositories;

import br.teste.hit.todoapi.domain.models.entities.UserTask;
import br.teste.hit.todoapi.domain.models.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    long countByStatus(TaskStatus status);
}
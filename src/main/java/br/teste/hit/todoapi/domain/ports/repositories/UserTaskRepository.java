package br.teste.hit.todoapi.domain.ports.repositories;

import br.teste.hit.todoapi.domain.models.entities.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
}
package br.teste.hit.todoapi.domain.models.dtos;

public record TaskStatisticDto(long totalTasks, long completedTasks, double percentageCompleted) {
}

package br.teste.hit.todoapi.domain.ports.mappers;

import br.teste.hit.todoapi.domain.models.dtos.CreateUpdateTaskDto;
import br.teste.hit.todoapi.domain.models.entities.UserTask;
import br.teste.hit.todoapi.domain.models.entities.UserTaskDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserTaskMapper {
    UserTask toEntity(CreateUpdateTaskDto createDto);

    UserTaskDto toDto(UserTask userTask);
    List<UserTaskDto> toDto(List<UserTask> userTasks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserTask partialUpdate(CreateUpdateTaskDto updateDto, @MappingTarget UserTask userTask);
}
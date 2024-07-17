package services;

import dto.TaskDto;
import entities.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    void save(TaskDto taskDto);

    void update(TaskDto taskDto);

    void delete(Task task);

    Optional<Task> findById(long id);

    List<Task> findAll();

    List<Task> findAllCompleted();

    List<Task> findAllNotCompleted();

    List<Task> findAllContaining(String part);

    List<Task> findAllContainingAndCompleted(String part);

    List<Task> findAllContainingAndNotCompleted(String part);

}

package services;

import dto.TaskDto;
import entities.Task;
import org.springframework.stereotype.Service;
import repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void save(TaskDto taskDto) {
        taskRepository.save(convertDtoToEntity(taskDto));
    }

    @Override
    public void update(TaskDto taskDto) {
        taskRepository.save(convertDtoToEntity(taskDto));
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findAllCompleted() {
        return taskRepository.findByCompletedTrue();
    }

    @Override
    public List<Task> findAllNotCompleted() {
        return taskRepository.findByCompletedFalse();
    }

    @Override
    public List<Task> findAllContaining(String part) {
        return taskRepository.findByNameContaining(part);
    }

    @Override
    public List<Task> findAllContainingAndCompleted(String part) {
        return taskRepository.findByCompletedTrueAndNameContaining(part);
    }

    @Override
    public List<Task> findAllContainingAndNotCompleted(String part) {
        return taskRepository.findByCompletedFalseAndNameContaining(part);
    }

    private Task convertDtoToEntity(TaskDto taskDto) {
        Task task = new Task();

        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExpirationDate(taskDto.getExpirationDate());
        task.setCategory(taskDto.getCategory());
        task.setCompleted(taskDto.isCompleted());

        return task;
    }

}

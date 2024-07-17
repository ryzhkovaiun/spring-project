package controllers;

import dto.TaskDto;
import entities.Filter;
import entities.Task;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class TasksController {

    private final TaskService taskService;

    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
        Model model
    ) {
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);

        if (keyword.isBlank()) {
            switch (filter) {
                case All -> model.addAttribute("tasks", taskService.findAll());
                case Completed -> model.addAttribute("tasks", taskService.findAllCompleted());
                case NotCompleted -> model.addAttribute("tasks", taskService.findAllNotCompleted());
            }
        } else {
            switch (filter) {
                case All -> model.addAttribute("tasks", taskService.findAllContaining(keyword));
                case Completed -> model.addAttribute("tasks", taskService.findAllContainingAndCompleted(keyword));
                case NotCompleted -> model.addAttribute("tasks", taskService.findAllContainingAndNotCompleted(keyword));
            }
        }

        return "tasks";
    }

    @GetMapping("/tasks/new")
    public String newTaskForm(
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
        Model model
    ) {
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("task", new TaskDto());

        return "new_task";
    }

    @PostMapping("/tasks/new")
    public String submitNewTask(
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
        @Valid @ModelAttribute("task") TaskDto taskDto
    ) {
        taskService.save(taskDto);

        return String.format("redirect:/tasks?filter=%s&keyword=%s", filter.name(), keyword);
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTaskForm(
        @PathVariable("id") long id,
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
        Model model
    ) {
        Optional<Task> task = taskService.findById(id);

        if (task.isEmpty()) {
            return String.format("redirect:/tasks?filter=%s&keyword=%s", filter.name(), keyword);
        }

        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("task", convertEntityToDto(task.get()));

        return "edit_task";
    }

    @PostMapping("/tasks/{id}")
    public String switchTaskCompleteState(
        @PathVariable("id") long id,
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        taskService.findById(id).ifPresent(this::switchTaskState);

        return String.format("redirect:/tasks?filter=%s&keyword=%s", filter.name(), keyword);
    }

    @PostMapping("/tasks/edit/{id}")
    public String editTask(
        @PathVariable("id") long id,
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
        @Valid @ModelAttribute("task") TaskDto taskDto
    ) {
        Optional<Task> task = taskService.findById(id);

        if (task.isEmpty()) {
            return String.format("redirect:/tasks?filter=%s&keyword=%s", filter.name(), keyword);
        }

        taskDto.setId(id);
        taskService.update(taskDto);

        return String.format("redirect:/tasks?filter=%s&keyword=%s", filter.name(), keyword);
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(
        @PathVariable("id") long id,
        @RequestParam(value = "filter", defaultValue = "All") Filter filter,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        taskService.findById(id).ifPresent(taskService::delete);

        return String.format("redirect:/tasks?filter=%s&keyword=%s", filter.name(), keyword);
    }

    private void switchTaskState(Task task) {
        task.setCompleted(!task.isCompleted());
        taskService.update(convertEntityToDto(task));
    }

    private TaskDto convertEntityToDto(Task task) {
        TaskDto taskDto = new TaskDto();

        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setExpirationDate(task.getExpirationDate());
        taskDto.setCategory(task.getCategory());
        taskDto.setCompleted(task.isCompleted());

        return taskDto;
    }

}
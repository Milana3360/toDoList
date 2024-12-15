package com.example.todo.Service;
import com.example.todo.Config.MessageResponse;
import com.example.todo.Config.Task;
import com.example.todo.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        if (isTaskDuplicate(task)) {
            throw new IllegalArgumentException("Task with the same title and description already exists.");
        }
        return taskRepository.save(task);
    }

    public boolean isTaskDuplicate(Task task) {
        return taskRepository.existsByTitleAndStatus(task.getTitle(), task.getStatus());
    }


    public Task updateTask(Long id, Task taskDetails) {
        if (taskRepository.existsById(id)) {
            taskDetails.setId(id);
            return taskRepository.save(taskDetails);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }
}

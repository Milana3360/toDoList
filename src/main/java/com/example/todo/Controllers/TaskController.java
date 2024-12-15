package com.example.todo.Controllers;
import com.example.todo.Config.MessageResponse;
import com.example.todo.Config.Task;
import com.example.todo.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Object> createTask(@RequestBody Task task) {
        if (taskService.isTaskDuplicate(task)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Task with the same title and status already exists"));
        }
        if (task.getDueDate() != null && task.getDueDate().isAfter(LocalDate.now()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("The 'dueDate' must be a past or present date"));

        }
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> existingTask = taskService.getTaskById(id);
        if (existingTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (taskService.isTaskDuplicate(taskDetails)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Task with the same title and status already exists"));
        }

        if (taskDetails.getDueDate() != null && taskDetails.getDueDate().isAfter(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("The 'dueDate' must be a past or present date"));
        }

        Task updatedTask = taskService.updateTask(id, taskDetails);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable String status) {
        return taskService.getTasksByStatus(status);
    }
}

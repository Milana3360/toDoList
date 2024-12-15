package com.example.todo.Repositories;
import com.example.todo.Config.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    boolean existsByTitleAndStatus(String title, String status);
}

package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
//    Optional<User> findByEmail(String email);


    List<Task> findAllByTaskGiver_Username(String taskGiver_username);
    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByTaskGiver_UsernameAndId(String taskGiver_username, UUID id);

    boolean existsByTaskGiver_UsernameAndId(String taskGiver_username, UUID id);

    // vaqtda kn gilarini ko'radi
    Optional<Task> findAllByDeadlineAfter(Timestamp deadline);

    //   vaqtda oldingilarini ko'radi
    Optional<Task> findAllByDeadlineBefore(Timestamp deadline);
}

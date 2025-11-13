package harjoitustyo.salisovellus.repository;

import harjoitustyo.salisovellus.model.WorkoutSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkoutSessionRepository extends CrudRepository<WorkoutSession, Long> {
    List<WorkoutSession> findByWorkoutIdOrderByDateDesc(Long workoutId);
}

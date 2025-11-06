package harjoitustyo.salisovellus.repository;

import harjoitustyo.salisovellus.model.Workout;
import org.springframework.data.repository.CrudRepository;

public interface WorkoutRepository extends CrudRepository<Workout, Long> {
}
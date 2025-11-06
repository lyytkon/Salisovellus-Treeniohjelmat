package harjoitustyo.salisovellus.repository;

import harjoitustyo.salisovellus.model.MuscleGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MuscleGroupRepository extends CrudRepository<MuscleGroup, Long> {
    Optional<MuscleGroup> findByName(String name);
}
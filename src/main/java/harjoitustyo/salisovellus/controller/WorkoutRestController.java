package harjoitustyo.salisovellus.controller;

import harjoitustyo.salisovellus.model.MuscleGroup;
import harjoitustyo.salisovellus.model.Workout;
import harjoitustyo.salisovellus.repository.MuscleGroupRepository;
import harjoitustyo.salisovellus.repository.WorkoutRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WorkoutRestController {

    private final WorkoutRepository workouts;
    private final MuscleGroupRepository groups;

    public WorkoutRestController(WorkoutRepository workouts, MuscleGroupRepository groups) {
        this.workouts = workouts;
        this.groups = groups;
    }

    @GetMapping("/workouts")
    public Iterable<Workout> allWorkouts() {
        return workouts.findAll();
    }

    @GetMapping("/workouts/{id}")
    public Optional<Workout> workout(@PathVariable Long id) {
        return workouts.findById(id);
    }

    @GetMapping("/groups")
    public Iterable<MuscleGroup> allGroups() {
        return groups.findAll();
    }
}
package harjoitustyo.salisovellus.controller;

import harjoitustyo.salisovellus.model.Workout;
import harjoitustyo.salisovellus.repository.MuscleGroupRepository;
import harjoitustyo.salisovellus.repository.WorkoutRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final MuscleGroupRepository muscleGroupRepository;

    public WorkoutController(WorkoutRepository workoutRepository, MuscleGroupRepository muscleGroupRepository) {
        this.workoutRepository = workoutRepository;
        this.muscleGroupRepository = muscleGroupRepository;
    }

    @GetMapping({"/", "/workouts"})
    public String list(Model model) {
        model.addAttribute("workouts", workoutRepository.findAll());
        return "workoutlist";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("workout", new Workout());
        model.addAttribute("groups", muscleGroupRepository.findAll());
        return "addworkout";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Workout workout) {
        // Thymeleaf bindaa vain muscleGroup.id:n, joten repository hoitaa entiteetin
        if (workout.getMuscleGroup() != null && workout.getMuscleGroup().getId() != null) {
            muscleGroupRepository.findById(workout.getMuscleGroup().getId())
                    .ifPresent(workout::setMuscleGroup);
        }
        workoutRepository.save(workout);
        return "redirect:/workouts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        workoutRepository.deleteById(id);
        return "redirect:/workouts";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Workout> w = workoutRepository.findById(id);
        if (w.isEmpty()) return "redirect:/workouts";
        model.addAttribute("workout", w.get());
        model.addAttribute("groups", muscleGroupRepository.findAll());
        return "editworkout";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
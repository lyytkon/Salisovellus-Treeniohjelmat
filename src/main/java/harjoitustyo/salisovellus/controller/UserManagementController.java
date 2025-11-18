package harjoitustyo.salisovellus.controller;

import harjoitustyo.salisovellus.model.User;
import harjoitustyo.salisovellus.repository.UserRepository;
import harjoitustyo.salisovellus.repository.WorkoutRepository;
import harjoitustyo.salisovellus.repository.WorkoutSessionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public UserManagementController(UserRepository userRepository, 
                                   WorkoutRepository workoutRepository,
                                   WorkoutSessionRepository workoutSessionRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.workoutSessionRepository = workoutSessionRepository;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "admin/userlist";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            // Poista käyttäjän workout sessionit
            user.getWorkouts().forEach(workout -> {
                workoutSessionRepository.deleteAll(
                    workoutSessionRepository.findByWorkoutIdOrderByDateDesc(workout.getId())
                );
            });
            // Poista käyttäjän workoutit
            workoutRepository.deleteAll(user.getWorkouts());
            // Poista käyttäjä
            userRepository.delete(user);
        });
        return "redirect:/admin/users";
    }
}

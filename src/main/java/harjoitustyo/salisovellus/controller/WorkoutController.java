package harjoitustyo.salisovellus.controller;

import harjoitustyo.salisovellus.model.User;
import harjoitustyo.salisovellus.model.Workout;
import harjoitustyo.salisovellus.model.WorkoutSession;
import harjoitustyo.salisovellus.repository.MuscleGroupRepository;
import harjoitustyo.salisovellus.repository.UserRepository;
import harjoitustyo.salisovellus.repository.WorkoutRepository;
import harjoitustyo.salisovellus.repository.WorkoutSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final MuscleGroupRepository muscleGroupRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserRepository userRepository;

    public WorkoutController(WorkoutRepository workoutRepository, 
                           MuscleGroupRepository muscleGroupRepository, 
                           WorkoutSessionRepository workoutSessionRepository,
                           UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.workoutSessionRepository = workoutSessionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/workouts")
    public String list(Model model, Authentication auth) {
        List<Workout> workouts;
        
        // Jos admin, näytä kaikki liikkeet
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            workouts = StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        } else {
            // Muille käyttäjille: näytä yhteiset liikkeet (user = null) + omat liikkeet
            User currentUser = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Käyttäjää ei löytynyt"));
            workouts = StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                    .filter(w -> w.getUser() == null || // Yhteiset liikkeet
                               (w.getUser() != null && w.getUser().getId().equals(currentUser.getId()))) // Omat liikkeet
                    .collect(Collectors.toList());
        }
        
        model.addAttribute("workouts", workouts);
        return "workoutlist";
    }

    @GetMapping("/workout/{id}")
    public String workoutDetail(@PathVariable Long id, Model model) {
        Optional<Workout> workout = workoutRepository.findById(id);
        if (workout.isEmpty()) return "redirect:/workouts";
        
        List<WorkoutSession> sessions = workoutSessionRepository.findByWorkoutIdOrderByDateDesc(id);
        
        model.addAttribute("workout", workout.get());
        model.addAttribute("sessions", sessions);
        return "workoutdetail";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/workouts";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("workout", new Workout());
        model.addAttribute("groups", muscleGroupRepository.findAll());
        return "addworkout";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Workout workout, Authentication auth) {
        if (workout.getMuscleGroup() != null && workout.getMuscleGroup().getId() != null) {
            muscleGroupRepository.findById(workout.getMuscleGroup().getId())
                    .ifPresent(workout::setMuscleGroup);
        }
        
        // Aseta käyttäjä
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Käyttäjää ei löytynyt"));
        workout.setUser(currentUser);
        
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

    @GetMapping("/addsession/{workoutId}")
    public String addSessionForm(@PathVariable Long workoutId, Model model) {
        Optional<Workout> w = workoutRepository.findById(workoutId);
        if (w.isEmpty()) return "redirect:/workouts";
        
        WorkoutSession session = new WorkoutSession();
        session.setWorkout(w.get());
        
        model.addAttribute("session", session);
        model.addAttribute("workout", w.get());
        return "addsession";
    }

    @PostMapping("/savesession")
    public String saveSession(@ModelAttribute WorkoutSession session, @RequestParam Long workoutId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        if (workout.isPresent()) {
            session.setWorkout(workout.get());
            workoutSessionRepository.save(session);
        }
        return "redirect:/workout/" + workoutId;
    }

    @GetMapping("/deletesession/{id}")
    public String deleteSession(@PathVariable Long id) {
        workoutSessionRepository.deleteById(id);
        return "redirect:/workouts";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
package harjoitustyo.salisovellus.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    // Kesto esim. "30min", "60min", "90min"
    private String duration;

    @ManyToOne
    @JoinColumn(name = "muscle_group_id")
    private MuscleGroup muscleGroup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutSession> sessions;

    public Workout() {}

    public Workout(Long id, String name, String description, String duration, MuscleGroup muscleGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.muscleGroup = muscleGroup;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public MuscleGroup getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(MuscleGroup muscleGroup) { this.muscleGroup = muscleGroup; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WorkoutSession> getSessions() { return sessions; }
    public void setSessions(List<WorkoutSession> sessions) { this.sessions = sessions; }
}
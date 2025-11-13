package harjoitustyo.salisovellus.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class WorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private LocalDate date;
    private Integer reps; // toistot
    private Double weight; // paino (kg)
    private Integer sets; // sarjat (valinnainen)

    public WorkoutSession() {}

    public WorkoutSession(Workout workout, LocalDate date, Integer reps, Double weight, Integer sets) {
        this.workout = workout;
        this.date = date;
        this.reps = reps;
        this.weight = weight;
        this.sets = sets;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Workout getWorkout() { return workout; }
    public void setWorkout(Workout workout) { this.workout = workout; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }
}

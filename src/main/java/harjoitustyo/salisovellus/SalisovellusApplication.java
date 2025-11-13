package harjoitustyo.salisovellus;

import harjoitustyo.salisovellus.model.MuscleGroup;
import harjoitustyo.salisovellus.model.Workout;
import harjoitustyo.salisovellus.repository.MuscleGroupRepository;
import harjoitustyo.salisovellus.repository.WorkoutRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalisovellusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalisovellusApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(MuscleGroupRepository groups, WorkoutRepository workouts) {
        return args -> {
            MuscleGroup chest = groups.save(new MuscleGroup(null, "Rinta"));
            MuscleGroup back = groups.save(new MuscleGroup(null, "Selkä"));
            MuscleGroup legs = groups.save(new MuscleGroup(null, "Jalat"));

            workouts.save(new Workout(null, "Penkkipunnerrus", "Voimapainotteinen perussetti", "60min", chest));
            workouts.save(new Workout(null, "Leuanveto", "Leveä myötäote, progressiivinen ylikuormitus", "30min", back));
            workouts.save(new Workout(null, "Takakyykky", "Tekniikkapainotteinen setti", "45min", legs));
        };
    }
}
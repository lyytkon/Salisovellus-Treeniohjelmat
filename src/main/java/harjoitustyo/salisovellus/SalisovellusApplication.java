package harjoitustyo.salisovellus;

import harjoitustyo.salisovellus.model.MuscleGroup;
import harjoitustyo.salisovellus.model.User;
import harjoitustyo.salisovellus.model.Workout;
import harjoitustyo.salisovellus.repository.MuscleGroupRepository;
import harjoitustyo.salisovellus.repository.UserRepository;
import harjoitustyo.salisovellus.repository.WorkoutRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SalisovellusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalisovellusApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(MuscleGroupRepository groups, UserRepository users, WorkoutRepository workouts, PasswordEncoder encoder) {
        return args -> {
            // Lihasryhmät
            MuscleGroup chest = groups.save(new MuscleGroup(null, "Rinta"));
            MuscleGroup back = groups.save(new MuscleGroup(null, "Selkä"));
            groups.save(new MuscleGroup(null, "Jalat"));
            groups.save(new MuscleGroup(null, "Olkapää"));
            groups.save(new MuscleGroup(null, "Kädet"));

            // Luo admin-käyttäjä jos ei ole olemassa
            User admin = null;
            if (users.findByUsername("admin").isEmpty()) {
                admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin"));
                admin.setRole("ADMIN");
                admin = users.save(admin);
                System.out.println("Admin-käyttäjä luotu: admin/admin");
            } else {
                admin = users.findByUsername("admin").get();
            }

            // Luo esimerkkiliikkeet admin-käyttäjälle
            Workout penkkipunnerrus = new Workout(null, "Penkkipunnerrus", "Perusliike rintalihasten kehittämiseen", "60min", chest);
            penkkipunnerrus.setUser(admin);
            workouts.save(penkkipunnerrus);

            Workout leuanveto = new Workout(null, "Leuanveto", "Leveä myötäote, progressiivinen ylikuormitus", "30min", back);
            leuanveto.setUser(admin);
            workouts.save(leuanveto);
            
            System.out.println("Esimerkkiliikkeet luotu admin-käyttäjälle");
        };
    }
}
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

import java.util.stream.StreamSupport;

@SpringBootApplication
public class SalisovellusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalisovellusApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(MuscleGroupRepository groups, UserRepository users, WorkoutRepository workouts, PasswordEncoder encoder) {
        return args -> {
            // Tarkista onko yhteiset liikkeet jo luotu
            long commonWorkoutsCount = StreamSupport.stream(workouts.findAll().spliterator(), false)
                    .filter(w -> w.getUser() == null)
                    .count();
            
            if (commonWorkoutsCount > 0) {
                System.out.println("Yhteiset liikkeet on jo luotu (" + commonWorkoutsCount + " kpl)");
                // Luo silti admin jos puuttuu
                if (users.findByUsername("admin").isEmpty()) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPasswordHash(encoder.encode("admin"));
                    admin.setRole("ADMIN");
                    users.save(admin);
                    System.out.println("Admin-käyttäjä luotu: admin/admin");
                }
                return;
            }
            
            // Lihasryhmät
            MuscleGroup chest = groups.save(new MuscleGroup(null, "Rinta"));
            MuscleGroup back = groups.save(new MuscleGroup(null, "Selkä"));
            MuscleGroup legs = groups.save(new MuscleGroup(null, "Jalat"));
            MuscleGroup shoulders = groups.save(new MuscleGroup(null, "Olkapää"));
            MuscleGroup arms = groups.save(new MuscleGroup(null, "Kädet"));
            MuscleGroup core = groups.save(new MuscleGroup(null, "Keskivartalo"));

            // Luo admin-käyttäjä jos ei ole olemassa
            if (users.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin"));
                admin.setRole("ADMIN");
                users.save(admin);
                System.out.println("Admin-käyttäjä luotu: admin/admin");
            }

            // Luo yleiset saliharjoitukset kaikille käyttäjille (user = null)
            // JALAT
            createCommonWorkout(workouts, "Kyykky", "Takakyykkä tangolla", legs);
            createCommonWorkout(workouts, "Jalkaprässi", "Jalkaprässi koneella", legs);
            createCommonWorkout(workouts, "Reiden ojennus", "Leg extension koneella", legs);
            createCommonWorkout(workouts, "Reiden koukistus", "Leg curl koneella", legs);
            createCommonWorkout(workouts, "Maastaveto", "Perinteinen maastaveto", legs);
            
            // RINTA
            createCommonWorkout(workouts, "Penkkipunnerrus", "Tasakaula-penkkipunnerrus tangolla", chest);
            createCommonWorkout(workouts, "Vinopenkki", "Vinopenkki käsipainoilla", chest);
            createCommonWorkout(workouts, "Dippi", "Rinnalle painottuva dippi", chest);
            createCommonWorkout(workouts, "Ristitalja", "Ristitalja ylhäältä", chest);
            
            // SELKÄ
            createCommonWorkout(workouts, "Leuanveto", "Leuanveto leveällä otteella", back);
            createCommonWorkout(workouts, "Ylätalja", "Ylätalja eteen leveällä otteella", back);
            createCommonWorkout(workouts, "Soutu", "Tankovenoutu", back);
            createCommonWorkout(workouts, "T-tanko soutu", "T-tankovenoutu", back);
            
            // OLKAPÄÄT
            createCommonWorkout(workouts, "Pystypunnerrus", "Pystypunnerrus tangolla seisten", shoulders);
            createCommonWorkout(workouts, "Olkapäiden nosto", "Sivunosto käsipainoilla", shoulders);
            createCommonWorkout(workouts, "Etunosto", "Etunosto käsipainoilla", shoulders);
            createCommonWorkout(workouts, "Takaolkapää", "Taakse nosto vatsalleen käsipainoilla", shoulders);
            
            // KÄDET
            createCommonWorkout(workouts, "Hauiskääntö", "Hauiskääntö tangolla", arms);
            createCommonWorkout(workouts, "Ojentajat köydellä", "Ojentajien työntö köydellä", arms);
            createCommonWorkout(workouts, "Vasaraote käännöt", "Hauiskääntö vasaraotteella", arms);
            createCommonWorkout(workouts, "French press", "Otsalle ojennus käsipainoilla", arms);
            
            // KESKIVARTALO
            createCommonWorkout(workouts, "Mave", "Mahalihasten rutistukset lattialla", core);
            createCommonWorkout(workouts, "Lankku", "Plank/Lankku vartalonhallintaan", core);
            createCommonWorkout(workouts, "Jalkojen nosto", "Jalkojen nosto riipputangosta", core);
            
            System.out.println("Yleiset saliharjoitukset luotu - käyttäjät voivat lisätä omia liikkeitä");
        };
    }
    
    private void createCommonWorkout(WorkoutRepository workouts, String name, String description, MuscleGroup muscleGroup) {
        Workout workout = new Workout(null, name, description, null, muscleGroup);
        workout.setUser(null); // Null = yhteinen liike kaikille
        workouts.save(workout);
    }
}
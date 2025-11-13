package harjoitustyo.salisovellus.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.withUsername("user")
                .passwordEncoder(encoder::encode)
                .password("user")
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .passwordEncoder(encoder::encode)
                .password("admin")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Permit all, mutta poisto vaatii ADMIN-roolin
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/delete/**").hasRole("ADMIN")  // Vain admin voi poistaa
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))  // H2-konsolille pois
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .formLogin(login -> login.loginPage("/login").permitAll())
            .logout(Customizer.withDefaults());
        return http.build();
    }
}
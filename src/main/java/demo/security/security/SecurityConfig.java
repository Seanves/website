package demo.security.security;

import demo.security.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.StandardPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;


    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/login","/register", "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and().formLogin().loginPage("/login")
                                  .loginProcessingUrl("/process_login")
                                  .defaultSuccessUrl("/", true).permitAll()
                .and().logout().logoutUrl("/process_logout")
                               .logoutSuccessUrl("/login")
                .and().userDetailsService(personDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder("saltOIGD");  //sha256
    }

}

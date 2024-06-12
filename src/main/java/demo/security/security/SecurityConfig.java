package demo.security.security;

import demo.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;


    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers("/login","/register", "/error", "/styles.css",
                                            "/images/**", "/js/**").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and().formLogin().loginPage("/login")
                                  .loginProcessingUrl("/process_login")
                                  .defaultSuccessUrl("/", true).permitAll()
                .and().logout().logoutUrl("/process_logout")
                               .logoutSuccessUrl("/login")
                .and().userDetailsService(userDetailsServiceImpl)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

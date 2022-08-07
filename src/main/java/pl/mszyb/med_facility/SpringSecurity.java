package pl.mszyb.med_facility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/**").authenticated()
                .antMatchers("/login", "/index").permitAll()
                .antMatchers("/afterlogin").hasRole("ADMIN")
               /* .anyRequest().hasRole("admin")*/
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/afterlogin").failureUrl("/403").and()
                .logout().logoutSuccessUrl("/login?test");

        return http.build();
    }

}

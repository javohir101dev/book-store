package uz.yt.springdata.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import uz.yt.springdata.auth.UserRoles;

import static uz.yt.springdata.auth.UserRoles.ADMIN;
import static uz.yt.springdata.auth.UserRoles.GUEST;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration{

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager config(){
        UserDetails userDetails = User.withUsername("user")
                .password(passwordEncoder().encode("123"))
//                .roles("USER")
                .authorities(GUEST.getPermissions())
                .build();

        UserDetails userDetails2 = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
//                .roles("USER")
                .authorities(ADMIN.getPermissions())
                .build();


        return new InMemoryUserDetailsManager(userDetails,  userDetails2);
    }
}

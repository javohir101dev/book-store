package uz.yt.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import uz.yt.springdata.auth.UserPermissions;
import uz.yt.springdata.auth.UserRoles;
import uz.yt.springdata.dao.Authorities;
import uz.yt.springdata.repository.AuthoritiesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@SpringBootApplication
@EnableCaching
@EnableRedisRepositories(basePackages = "uz.yt.springdata.redis")
public class SpringDataApplication implements CommandLineRunner {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (Objects.requireNonNull(environment.getProperty("create.roles")).equalsIgnoreCase("true")) {
            List<Authorities> allAuthoritiesForSaving = new ArrayList<>();
            allAuthoritiesForSaving.addAll(Arrays.stream(
                    UserPermissions.values())
                    .map(e -> new Authorities(e.getId(), e.getName()))
                    .collect(Collectors.toList())
            );
            allAuthoritiesForSaving.addAll(
                    Arrays.stream(
                            UserRoles.values())
                            .map(e -> new Authorities(e.getId(), "ROLE_" +  e.getName()))
                            .collect(Collectors.toList())
            );
            List<Authorities> allSavedAuthorities = authoritiesRepository.findAll();
            allAuthoritiesForSaving.removeAll(allSavedAuthorities);
            authoritiesRepository.saveAll(allAuthoritiesForSaving);
        }
    }
}

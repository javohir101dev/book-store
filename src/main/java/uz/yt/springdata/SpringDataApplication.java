package uz.yt.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uz.yt.springdata.auth.UserRoles;
import uz.yt.springdata.dao.Authorities;
import uz.yt.springdata.repository.AuthoritiesRepository;


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
		if (environment.getProperty("create.roles").equalsIgnoreCase("true")){
			for (UserRoles u : UserRoles.values()){
				Authorities auth = new Authorities();
				auth.setAuthority(u.getName());
				authoritiesRepository.save(auth);
			}
		}
	}
}

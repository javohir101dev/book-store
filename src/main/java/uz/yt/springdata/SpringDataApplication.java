package uz.yt.springdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@EnableCaching
@EnableRedisRepositories(basePackages = "uz.yt.springdata.redis")
public class SpringDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataApplication.class, args);
	}
}

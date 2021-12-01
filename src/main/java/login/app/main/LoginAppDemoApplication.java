package login.app.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("login.app")
@MapperScan("login.app.mapper")
public class LoginAppDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginAppDemoApplication.class, args);
	}
}

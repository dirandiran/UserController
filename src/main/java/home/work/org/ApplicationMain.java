package home.work.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@PropertySource(value = { "application.properties" }, ignoreResourceNotFound = true)
//@EnableSwagger2
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}

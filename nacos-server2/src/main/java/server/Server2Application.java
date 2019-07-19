package server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：zgq.
 * @Date ：Created in 15:33 2019/7/19
 * @Description：
 * @Modified By：
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Server2Application {

    public static void main(String[] args) {
        SpringApplication.run(Server2Application.class, args);
    }

    @Slf4j
    @RestController
    static class Test2Controller {
        @GetMapping("/hello")
        public String hello(@RequestParam String name) {
            log.info("invoked name = " + name);
            return "hello " + name;
        }
    }
}

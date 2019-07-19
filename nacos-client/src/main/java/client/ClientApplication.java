package client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author ：zgq.
 * @Date ：Created in 15:33 2019/7/19
 * @Description：
 * @Modified By：
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        @Autowired
        LoadBalancerClient loadBalancerClient;

        @GetMapping("/test")
        public String test() {
            // 通过spring cloud common中的负载均衡接口选取服务提供节点实现接口调用
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-server");
            String url = serviceInstance.getUri() + "/hello?name=" + "didi";
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            return "Invoke : " + url + ", return : " + result;
        }
    }

    @Slf4j
    @RestController
    static class Test2Controller {

        @Autowired
        RestTemplate restTemplate;

        @GetMapping("/test2")
        public String test() {
            String result = restTemplate.getForObject("http://nacos-server/hello?name=didi", String.class);
            return "Return : " + result;
        }
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Slf4j
    @RestController
    static class Test3Controller {

        @Autowired
        private WebClient.Builder webClientBuilder;

        @GetMapping("/test3")
        public Mono<String> test() {
            Mono<String> result = webClientBuilder.build()
                    .get()
                    .uri("http://nacos-server/hello?name=didi")
                    .retrieve()
                    .bodyToMono(String.class);
            return result;
        }
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }


    @Slf4j
    @RestController
    static class Test4Controller {

        @Autowired
        Client client;

        @GetMapping("/test4")
        public String test() {
            String result = client.hello("didi");
            return "Return : " + result;
        }
    }


    @FeignClient("nacos-server")
    interface Client {

        @GetMapping("/hello")
        String hello(@RequestParam(name = "name") String name);

    }
}

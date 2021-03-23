package io.github.andersori.message.publisher;

import io.github.andersori.message.publisher.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@SpringBootApplication
public class App {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitConfig config;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public void initiateShutdown(int returnCode){
        SpringApplication.exit(context, () -> returnCode);
    }

    @Bean
    CommandLineRunner run(){
        return args -> {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
                log.info("Please, type some word!!");
                String line = br.readLine();
                while (line != null && (!line.isBlank() && !line.trim().equalsIgnoreCase("no"))){
                    template.convertAndSend(config.getExchangeName(), config.getRouteKey(), line);
                    log.info("Another one?");
                    line = br.readLine();
                }
                initiateShutdown(0);
            } catch (Exception e) {
                initiateShutdown(1);
            }
        };
    }
}

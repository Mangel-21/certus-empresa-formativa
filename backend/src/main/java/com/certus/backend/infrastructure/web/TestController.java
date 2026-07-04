package com.certus.backend.infrastructure.web;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.certus.backend.infrastructure.config.RabbitMQConfig;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class TestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("api", "OK");

        try {
            redisTemplate.opsForValue().set("test_key", "Redis connection successful");
            String redisValue = (String) redisTemplate.opsForValue().get("test_key");
            status.put("redis", redisValue != null ? "OK" : "FAILED");
        } catch (Exception e) {
            status.put("redis", "FAILED: " + e.getMessage());
        }

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, "Test Notification");
            status.put("rabbitmq", "OK");
        } catch (Exception e) {
            status.put("rabbitmq", "FAILED: " + e.getMessage());
        }

        return status;
    }
}

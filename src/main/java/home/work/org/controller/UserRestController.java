package home.work.org.controller;

import home.work.org.entity.RiskProfile;
import home.work.org.entity.User;
import home.work.org.repository.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.Optional;

@EnableKafka
@RestController
@Transactional(isolation = Isolation.SERIALIZABLE)
public class UserRestController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private UserRepository userRepository;
    @Value("${users.url}")
    private String url;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/", produces = "application/text")
    public String getMapping() {
        return " Mapping:\n" +
                "Http.GET: /users - get all users\n" +
                "Http.GET: /user/id - get user by id\n" +
                "Http.POST: /update/{id}/{riskProfile} - update User by id\n" +
                "Http.PUT: /save/{riskProfile} - add new User\n" +
                "Http.DELETE: /delete/{id} - delete User by id\n" +
                "Http.POST: /merge/{id1}/{id2} - merge two users by their id\n" +
                "If profile = 'h2'\n" +
                "h2 console: http://localhost:8085/h2\n" +
                "swagger ui: http://localhost:8085/swagger-ui.html";
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @GetMapping(path = "/users", produces = "application/json")
    public List<User> getUsers() {
        return (List<User>) (userRepository.findAll());
    }

    @CrossOrigin(origins = "*")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @GetMapping(path = "user/{id}")
    public Optional<User> getUsersById(@PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

    @PostMapping(path = "/update/{id}/{riskProfile}")
    public User updateUserById(@PathVariable("id") Long id, @PathVariable("riskProfile") RiskProfile riskProfile) {
        return userRepository.save(new User(id, riskProfile));
    }

    @PutMapping(path = "/save/{riskProfile}")
    public User createUser(@PathVariable("riskProfile") RiskProfile riskProfile) {
        return userRepository.save(new User(riskProfile));
    }

    @DeleteMapping(path = "/delete/{id}")
    public User deleteUserById(@PathVariable("id") Long id) {
        User userForDel = userRepository.findById(id).orElse(null);
        userRepository.deleteById(id);
        return userForDel;
    }

    @PostMapping(path = "/merge/{firstId}/{secondId}")
    public User mergeUsersById(@PathVariable("firstId") Long firstId, @PathVariable("secondId") Long secondId) {

        if (userRepository.existsById(firstId) && userRepository.existsById(secondId)) {
            List<User> userPair = userRepository.getUsersForMerge(firstId, secondId);
            User userByFirstId = userPair.get(0);
            User userBySecondId = userPair.get(1);
            if (userByFirstId.compareTo(userBySecondId) >= 0) {
                userRepository.deleteMergedUsers(firstId, secondId);
                return userRepository.save(new User(userByFirstId.getRiskProfile()));
            } else {
                userRepository.deleteMergedUsers(firstId, secondId);
                return userRepository.save(new User(userBySecondId.getRiskProfile()));
            }
        } else {
            return null;
        }
    }

    @GetMapping(path = "/send")
    public void produceMessageToKafka() {
        for (int i = 0; i < 10; i++) {
            String key = String.valueOf(Math.round(Math.random() * 1000));
            double value = new Double(Math.round(Math.random() * 10000000L)).intValue() / 1000.0;
            JsonObject json = Json.createObjectBuilder()
                    .add("id", key)
                    .add("price", value)
                    .build();
//            kafkaTemplate.send("test", 0, (long) i, "Hello World! " + i);
            kafkaTemplate.send(new ProducerRecord<>("test", 2, key, json.toString()));
        }
    }

    @KafkaListener(topics = "test", groupId = "app.1")
    private void printMessageFromTopicTest1(String msg) {
        System.out.println("Consumer-1: " + msg);
    }

    @KafkaListener(topics = "test", groupId = "app.1", containerFactory = "kafkaListenerContainerFactory")
    private void printMessageFromTopicTest2(List<ConsumerRecord<String, String>> consumerRecords) {
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            System.out.println("Consumer-2: " + "offset:" + consumerRecord.offset() + " key:" + consumerRecord.key() + " value:" + consumerRecord.value());
        }
    }
}

package home.work.org.controller;

import home.work.org.entity.RiskProfile;
import home.work.org.entity.User;
import home.work.org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
@Transactional(isolation = Isolation.SERIALIZABLE)
public class UserRestController {

    private UserRepository userRepository;
    @Value("${users.url}")
    private String url;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/", produces = "application/json")
    public String getMapping() {
        return "Mapping:\n" +
                "/users - get all users\n" +
                "/user/id - get user by id";
    }

    @Transactional( propagation = Propagation.SUPPORTS,readOnly = true )
    @GetMapping(path = "/users", produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return (List<User>) (userRepository.findAll());
    }

    @Transactional( propagation = Propagation.SUPPORTS,readOnly = true )
    @GetMapping(path = "user/{id}")
    @ResponseBody
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
    public RedirectView deleteUserById(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return new RedirectView(url + "/users");
    }

    @PostMapping(path = "/merge/{firstId}/{secondId}")
    public User mergeUsersById(@PathVariable("firstId") Long firstId, @PathVariable("secondId") Long secondId) {

        if (userRepository.existsById(firstId) && userRepository.existsById(secondId)) {
            List<User> userPair = userRepository.getUsersForMerge(firstId, secondId);
            User userByFirstId = userPair.get(0);
            User userBySecondId = userPair.get(1);
            if (userByFirstId.compareTo(userBySecondId) >= 0) {
                userRepository.deleteMergedUsers(firstId,secondId);
                return userRepository.save(new User(userByFirstId.getRiskProfile()));
            } else {
                userRepository.deleteMergedUsers(firstId,secondId);
                return userRepository.save(new User(userBySecondId.getRiskProfile()));
            }
        } else {
            return null;
        }
    }
}

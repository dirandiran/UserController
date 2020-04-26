package home.work.org;

import home.work.org.entity.RiskProfile;
import home.work.org.entity.User;
import home.work.org.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = ApplicationMain.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void findById(){
        User userEtalon = new User(RiskProfile.LOW);
        userEtalon = entityManager.persist(userEtalon);
        entityManager.flush();

        User foundUser = userRepository.findById(userEtalon.getId()).orElse(null);

        assertThat(foundUser.getRiskProfile())
                .isEqualTo(userEtalon.getRiskProfile());
    }


}
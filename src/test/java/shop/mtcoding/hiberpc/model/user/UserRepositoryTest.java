package shop.mtcoding.hiberpc.model.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.config.dummy.MyDummyEntity;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Import(UserRepository.class)
@DataJpaTest
public class UserRepositoryTest extends MyDummyEntity {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        User user = newUser("ssar");
        userRepository.save(user);
    }

    @Test
    public void save_test() {
        // given

        // when
        User userPS = userRepository.findAll().get(0);

        // then
        Assertions.assertThat(userPS.getId()).isEqualTo(1);
    }

    @Test
    public void update_test() {
        // given 1 - DB에 영속화

        // given 2 - request 데이터
        String password = "5678";
        String email = "ssar@gmail.com";

        User userPS = userRepository.findAll().get(0);

        // when
        userPS.update(password, email);
        User updateUserPS = userRepository.save(userPS);

        // then
        Assertions.assertThat(updateUserPS.getPassword()).isEqualTo("5678");
    }

    @Test
    public void update_dirty_checking_test() {
        // given 1 - DB에 영속화

        User userPS = userRepository.findAll().get(0);
        // given 2 - request 데이터
        String password = "5678";
        String email = "ssar@gmail.com";

        // when
        userPS.update(password, email);
        em.flush();

        // then
        User updateUserPS = userRepository.findById(1);  // DB에 잘 들어 갔는지 확인하는 절차
        Assertions.assertThat(updateUserPS.getPassword()).isEqualTo("5678");
    }

    @Test
    public void delete_test() {
        // given 1 - DB에 영속화

        User userPS = userRepository.findAll().get(0);
        // given 2 - request 쿼리스트링 데이터
        int id = 1;
        User findUserPS = userRepository.findById(id); // DB에 들어 가는 것이아니라 PC에 넣고 영속화
        // when
        userRepository.delete(findUserPS);

        // then
        User deleteUserPS = userRepository.findById(1);  // DB에 잘 들어 갔는지 확인하는 절차
        Assertions.assertThat(deleteUserPS).isNull();
    }
    @Test
    public void findById_test() {
        // given 1 - DB에 영속화

        // given 2
        int id = 1;

        // when
        User userPS = userRepository.findById(id);

        // then
        Assertions.assertThat(userPS.getUsername()).isEqualTo("ssar");

    }

    @Test
    public void findAll_test() {
        // given
        List<User> userList = Arrays.asList(newUser("love"), newUser("cos"));
        userList.stream().forEach((user)->{
            userRepository.save(user);
        });

        // when
        List<User> userListPS = userRepository.findAll();
        System.out.println("테스트 : " + userListPS);

        // then
        Assertions.assertThat(userListPS.size()).isEqualTo(3);
    }


}

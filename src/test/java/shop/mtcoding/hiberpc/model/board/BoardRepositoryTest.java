package shop.mtcoding.hiberpc.model.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.config.dummy.MyDummyEntity;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Import({UserRepository.class , BoardRepository.class})
@DataJpaTest
public class BoardRepositoryTest extends MyDummyEntity {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save_test() {
        // given 1
        User user = newUser("ssar");
        User userPS = userRepository.save(user);

        // given 2
        Board board = newBoard("제목1", userPS);

        // when
        Board boardPS = boardRepository.save(board);
        System.out.println("테스트 : " + boardPS);

        // then
        Assertions.assertThat(boardPS.getId()).isEqualTo(1);
        Assertions.assertThat(boardPS.getUser().getId()).isEqualTo(1);
    }

    //더디체킹
    @Test
    public void update_test() {
        // given 1 - DB에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

        // given 2 - request 데이터
        String title = "제목12";
        String content = "내용12";

        // when
        boardPS.update(title, content);
        em.flush();
        // then
        Board findBoardPS = boardRepository.findById(1);
        Assertions.assertThat(findBoardPS.getTitle()).isEqualTo("제목12");
    }


    @Test
    public void delete_test() {
        // given 1 - DB에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

        // given 2 - request 쿼리스트링 데이터
        int id = 1;
        Board findBoardPS = boardRepository.findById(id); // DB에 들어 가는 것이아니라 PC에 넣고 영속화

        // when
        boardRepository.delete(findBoardPS);

        // then
        Board deleteBoardPS = boardRepository.findById(1);  // DB에 잘 들어 갔는지 확인하는 절차
        Assertions.assertThat(deleteBoardPS).isNull();
    }
    @Test
    public void findById_test() {
        // given 1 - DB에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        boardRepository.save(board);

        // given 2
        int id = 1;

        // when
        Board findBoardPS = boardRepository.findById(id);

        // then
        Assertions.assertThat(findBoardPS.getUser().getUsername()).isEqualTo("ssar");
        Assertions.assertThat(findBoardPS.getTitle()).isEqualTo("제목1");

    }

    @Test
    public void findAll_test() {
        // given
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        List<Board> boardList = Arrays.asList(newBoard("ssar",userPS), newBoard("cos",userPS));
        boardList.stream().forEach((board)->{
            boardRepository.save(board);
        });

        // when
        List<Board> boardListPS = boardRepository.findAll();
        System.out.println("테스트 : " + boardListPS);

        // then
        Assertions.assertThat(boardListPS.size()).isEqualTo(2);
    }


}

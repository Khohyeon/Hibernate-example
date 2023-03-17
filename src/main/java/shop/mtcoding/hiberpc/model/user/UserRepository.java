package shop.mtcoding.hiberpc.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;
    public User findById(int id){
        return em.find(User.class, id);
    }
    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class).getResultList();
    }
    public User save(User user){
        if(user.getId() == null){
            em.persist(user);
        }else { // 더티채킹 할 것 이기 때문에 사용할 일이 없다.
            User userPS = em.find(User.class, user.getId());
            if (userPS != null) {
                em.merge(user);
            }else {
                System.out.println("잘못된 머지를 하셨어요!!");
            }
        }
        return user;
    }
    public void delete(User user){
        em.remove(user);
    }

}

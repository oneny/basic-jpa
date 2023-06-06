package oneny.jpql;


import oneny.jpql.domain.Member;
import oneny.jpql.domain.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQLMainDTO {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member = new Member();
      member.setUsername("member1");
      member.setAge(10);
      em.persist(member);

      em.flush();
      em.clear();

      // Object[]로 타입 Casting하는 방법이 있지만 더 좋은 방법이 있음
//      List<Object[]> result = em.createQuery("select m.name, m.age from Member m")
//              .getResultList();

      // Entity가 아닌 경우에는 new 키워드를 사용해야 한다.
      List<UserDTO> result = em.createQuery("select new oneny.jpql.domain.UserDTO(m.username, m.age) from Member m", UserDTO.class)
              .getResultList();

      UserDTO userDTO = result.get(0);

      System.out.println("userDTO = " + userDTO);


      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

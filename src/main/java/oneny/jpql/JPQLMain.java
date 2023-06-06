package oneny.jpql;


import oneny.jpql.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQLMain {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member = new Member();
      member.setUsername("member1");
      em.persist(member);

      em.flush();
      em.clear();

      List<Member> result = em.createQuery("select m from Member m", Member.class)
              .getResultList();

      // 모두 영속성 컨텍스트에 관리되기 때문에 update 쿼리가 실행된다.
      Member findMember = result.get(0);
      findMember.setAge(20);

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

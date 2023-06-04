package oneny;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
    /*
      // 회원 등록
      Member member = new Member();

      member.setId(1L);
      member.setName("oneny");

      em.persist(member);
    */
      Member findMember = em.find(Member.class, 1L);
      System.out.println("findMember.getId() = " + findMember.getId());
      System.out.println("findMember.getName() = " + findMember.getName());

      findMember.setName("twony");

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

package oneny.jpql;


import oneny.jpql.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQLMainPaging {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      for (int i = 0; i < 100; i++) {
        Member member = new Member();
        member.setUsername("member" + i);
        member.setAge(i);
        em.persist(member);
      }

      em.flush();
      em.clear();

      // setFirstResult(int startPosition): 조회 시작 위치(0부터 시작)
      // setMaxResult(int maxResult): 조회할 데이터 수
      List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
              .setFirstResult(1) // offset
              .setMaxResults(10) // limit
              .getResultList();

      System.out.println("result.size() = " + result.size());
      for (Member findMember : result) {
        System.out.println("findMember = " + findMember);
      }

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

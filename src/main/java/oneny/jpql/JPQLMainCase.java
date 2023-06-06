package oneny.jpql;


import oneny.jpql.domain.Member;
import oneny.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static oneny.jpql.domain.MemberType.ADMIN;

public class JPQLMainCase {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Team team = new Team();
      team.setName("teamA");
      em.persist(team);

      Member member = new Member();
      member.setUsername("oneny");
      member.setAge(10);
      member.setTeam(team);
      member.setType(ADMIN);
      em.persist(member);

      em.flush();
      em.clear();

      String query =
              "select " +
                      "case when m.age <= 10 then '학생요금' " +
                      "     when m.age >= 60 then '경로요금' " +
                      "     else '일반요금' " +
                      "end " +
                      "from Member m";

      List<String> result = em.createQuery(query, String.class)
              .getResultList();
      for (String cost : result) {
        System.out.println("cost = " + cost);
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

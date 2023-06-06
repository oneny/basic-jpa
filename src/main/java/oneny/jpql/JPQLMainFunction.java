package oneny.jpql;


import oneny.jpql.domain.Member;
import oneny.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static oneny.jpql.domain.MemberType.ADMIN;
import static oneny.jpql.domain.MemberType.USER;

public class JPQLMainFunction {
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

      Member member2 = new Member();
      member2.setUsername("twony");
      member2.setAge(60);
      member2.setTeam(team);
      member2.setType(USER);
      em.persist(member2);

      em.flush();
      em.clear();

      // JPA 용 기본 함수(SIZE, INDEX)
      String query = "select size(t.members) from Team t";
      List<Integer> result = em.createQuery(query, Integer.class)
              .getResultList();

      for (Integer count : result) {
        System.out.println("count = " + count);
      }

      // 사용자 정의 함수
//      String query2 = "select function('group_concat', m.username) from Member m";
      String query2 = "select group_concat(m.username) from Member m";
      List<String> result2 = em.createQuery(query2, String.class)
              .getResultList();

      for (String s : result2) {
        System.out.println("s = " + s);
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

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

public class JPQLMainNamedQuery {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Team team = new Team();
      team.setName("teamA");
      em.persist(team);

      Team team2 = new Team();
      team2.setName("teamB");
      em.persist(team2);

      Member member = new Member();
      member.setUsername("회원1");
      member.setAge(10);
      member.setTeam(team);
      member.setType(ADMIN);
      em.persist(member);

      Member member2 = new Member();
      member2.setUsername("회원2");
      member2.setAge(60);
      member2.setTeam(team);
      member2.setType(USER);
      em.persist(member2);

      Member member3 = new Member();
      member3.setUsername("회원2");
      member3.setAge(40);
      member3.setTeam(team2);
      member3.setType(USER);
      em.persist(member3);

      em.flush();
      em.clear();

      List<Member> result = em.createNamedQuery("Member.findByUsername", Member.class)
              .setParameter("username", "회원1")
              .getResultList();

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

package oneny.jpql;


import oneny.jpql.domain.Member;
import oneny.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPQLMainJoin {
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
      member.setUsername("member1");
      member.setAge(10);
      member.setTeam(team);
      em.persist(member);

      em.flush();
      em.clear();

      // 내부 조인: SELECT m FROM Member m [INNER] JOIN m.team t
      // 외부 조인: SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
      // 세타 조인: SELECT COUNT(m) FROM Member m, Team t WHERE m.username = t.name
      String query = "select m from Member m inner join m.team t";
      List<Member> result = em.createQuery(query, Member.class)
              .getResultList();

      // 조인 - ON절
      // 조인 대상 필터링, "연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)

      // 조인 대상 필터링 - ex) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
      // JPQL - SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'A'
      // SQL - SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.name = 'A'

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

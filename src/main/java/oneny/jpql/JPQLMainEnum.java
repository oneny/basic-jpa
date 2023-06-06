package oneny.jpql;


import oneny.jpql.domain.Member;
import oneny.jpql.domain.MemberType;
import oneny.jpql.domain.Team;
import org.hibernate.usertype.UserType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static oneny.jpql.domain.MemberType.ADMIN;

public class JPQLMainEnum {
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

      // 패키지명까지 포함해야 한다.
//      String query = "select m from Member m where m.type = oneny.jpql.domain.MemberType.ADMIN";
//      List<Member> result = em.createQuery(query, Member.class)
//              .getResultList();
      String query = "select m from Member m where m.type = :userType";
      List<Member> result = em.createQuery(query, Member.class)
              .setParameter("userType", ADMIN) // 파라미터 바인딩으로 가능
              .getResultList();


      for (Member findMember : result) {
        System.out.println("findMember = " + findMember);
      }

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

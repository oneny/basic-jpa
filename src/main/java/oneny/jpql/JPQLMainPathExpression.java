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

public class JPQLMainPathExpression {
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

      // 상태 필드(state field): 경로 탐색의 끝. 탐색 X -> select m.username from Member m
      // 단일 값 연관 경로: 묵시적 내부 조인(inner join) 발생. 탐색 O -> select m.team.name from Member m
      // 컬렉션 값 연관 경로: 묵시적 내부 조인 발생. 탐색 X -> select t.members from Team t
      //    - FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
      // 묵시적 내부 조인이 일어나는만큼 조심해야 사용해야 한다.
      // 묵시적 내부 조인이 일어나지 않도록 하는 것이 좋다.
      String query = "select m.team.name from Member m";
      List<String> result = em.createQuery(query, String.class)
              .getResultList();

      for (String name : result) {
        System.out.println("count = " + name);
      }

      // 명시적 조인을 통한 컬렉션 값 연관 경로
      // members가 컬렉션이기 때문에 select t.members.username from Team t는 실패한다.
      // 명시적 조인을 통해 members를 가져온 다음 alias m을 통해 가져왔기 때문에 성공한다.
      String query2 = "select m.username from Team t join t.members m";
      List<String> result2 = em.createQuery(query2, String.class)
              .getResultList();

      System.out.println("result = " + result2);

      // 참고
      // 명시적 조인: join 키워드 족접 사용 -> select m from Member m join m.team t
      // 묵시적 조인: 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능) -> select m.team from Member m

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

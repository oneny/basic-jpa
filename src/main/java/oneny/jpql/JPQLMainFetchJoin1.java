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

public class JPQLMainFetchJoin1 {
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

//      String query = "select m from Member m";
//      List<Member> result = em.createQuery(query, Member.class)
//              .getResultList();
//
//      for (Member findMember : result) {
//        System.out.println("findMember = " + findMember);
//        System.out.println("findMember.getTeam() = " + findMember.getTeam().getClass()); // getTeam할 때 쿼리가 실행된다.
//      }

      // 엔티티 패치 조인
      // 회원 100명을 회원 + 팀 조회해야하는 경우가 발생하면 N + 1 문제가 발
      // 즉시로딩하는 상황과 같은 방식으로 해결 가능. 쿼리로 어떤 객체를 한 번에 조회할 것인지를 명시적으로 작성할 수 있다.
      // ex) 회원을 조회하면서 연관된 팀도 함꼐 조회(SQL 한 번에)
      // [JPQL] select m from Member m join fetch m.team
      // [SQL] select M.*, T.* from Member m inner join Team t on m.team_id = t.team_id
      String query2 = "select m from Member m join fetch m.team";
      List<Member> result2 = em.createQuery(query2, Member.class)
              .getResultList();

      // 이때 한 가지 재미있는 점이 위에 주석을 풀게 되면 team은 Proxy이기 때문에 영속성 컨텍스트에는 프록시 객체로 관리가 되기 떄문에
      // 아래 루프를 돌면서 getClass를 하는 경우에 프록시가 출력되고
      // 주석을 풀면 Team 클래스가 출력된다.
      for (Member findMember : result2) {
        System.out.println("findMember = " + findMember);
        System.out.println("findMember = " + findMember.getTeam().getClass()); // 한 번에 조회했기 때문에 쿼리가 또 실행되지 않는다.
      }

      // 컬렉션 패치 조인
      // [JPQL] select t from Team t join fetch t.members where t.name = 'TeamA'
      // [SQL] select t.*, m.* inner join member m on t.TEAM_ID = m.TEAM_ID where t.name = 'teamA'
      String query3 = "select distinct t from Team t join fetch t.members";
      List<Team> result3 = em.createQuery(query3, Team.class)
              .getResultList();

      // Team x Member는 1 x m 레벨이여서 결국 List는 m 레벨로 들어간다. -> 데이터 뻥튀기가 되기 때문에 루프를 3번 돈다.
      // JPQL의 distinct로 해결이 가능하다 -> SQL에 DISTINCT(team.team_id) 추가, 애플리케이션에서 엔티티 중복 제거
      for (Team findTeam : result3) {
        System.out.println("findTeam.getName() = " + findTeam.getName() + ", size = " + findTeam.getMembers().size());

        for (Member findMember : findTeam.getMembers()) {
          System.out.println("  -> findMember = " + findMember);
        }
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

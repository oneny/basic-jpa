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

public class JPQLMainFetchJoin2 {
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

      // 페치 조인의 특징과 한계
      //   - 페치 조인 대상에는 별칭을 줄 수 없음(하이버네이트는 가능하지만 가급적 사용 X)
      // 둘 이상의 컬렉션은 페치 조인할 수 없음
      // 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없음
      //   - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
      //   - 하이버네이트는 경고 로그를 남기고 메모리에서 페이지(매우 위험)

      // 페이징 관련해서는 다대일로 쿼리를 작성하는 것이 좋다.
//      String query = "select t from Team t join fetch t.members"; // 매우 위험
//      String query = "select m from Member m join fetch m.team";
//
//      List<Member> result = em.createQuery(query, Member.class)
//              .setFirstResult(0)
//              .setMaxResults(2)
//              .getResultList();
//
//      for (Member findMember : result) {
//        System.out.println("findMember = " + findMember);
//      }


      // Batch
      // batch를 하지 않는 경우에는 select 팀, 팀과 관련된 members 조회 2번 => 총 3번 쿼리가 실행되어 비효율적이다.
      // Team 엔티티에 member에 @BatchSize 어노테이션을 사용하면 IN을 통해 teamA와 teamB와 관련된 members를 모두 가져온다.
      // <property name="hibernate.default_batch_fetch_size" value="100"/> 설정으로 디폴트 배치사이즈를 설정할 수 있다.
      // 실무에서 글로벌 로딩 전략은 모두 지연 로딩
      // 최적화가 필요한 곳은 페치 조인 적용을 한다.
      String query2 = "select t from Team t";
      List<Team> result2 = em.createQuery(query2, Team.class)
              .setFirstResult(0)
              .setMaxResults(2)
              .getResultList();

      for (Team findTeam : result2) {
        System.out.println("findTeam = " + findTeam + ", members = " + findTeam.getMembers().size());

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

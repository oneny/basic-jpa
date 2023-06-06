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

public class JPQLMainBulk {
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

//      em.flush();
//      em.clear();

      // 벌크 연산 주의
      // 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리하기 때문에 아래와 같은 방법 중 하나를 선택해서 해겷해야 한다.
      //   벌크 연산을 먼저 실행
      //   벌크 연산 수행 후 영속성 컨텍스트 초기화
      // createQuery는 자동 Flush 호출
      int resultCount = em.createQuery("update Member m set m.age = 20")
              .executeUpdate();

      System.out.println("resultCount = " + resultCount);

      System.out.println("member2 = " + member2); // age가 60

      // 영속성 컨텍스트에서 1차 캐시에 있는 member 들을 제거해줘야 한다.
      // 아니면 다시 find해도 업데이트되지 않은 member를 반환한다.
      em.clear();

      Member findMember = em.find(Member.class, member.getId());

      System.out.println("findMember = " + findMember); // age가 20

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

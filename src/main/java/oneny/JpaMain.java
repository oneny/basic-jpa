package oneny;

import oneny.domain.Address;
import oneny.domain.Book;
import oneny.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Address address = new Address("city", "street", "10000");

      Member member = new Member();
      member.setUsername("member1");
      member.setHomeAddress(address);
      em.persist(member);

      Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

      Member member2 = new Member();
      member2.setUsername("member2");
      member2.setHomeAddress(address);
      em.persist(member2);

//      member.getHomeAddress().setCity("newCity");
      member.changeCityOfAddress("newCity"); // 생성 시점 이후 절대 값을 변경할 수 없는 불변 객체를 활용하면 member2는 바뀌지 않음

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

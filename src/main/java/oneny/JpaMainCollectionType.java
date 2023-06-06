package oneny;

import oneny.domain.Address;
import oneny.domain.AddressEntity;
import oneny.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainCollectionType {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member = new Member();
      member.setUsername("member1");
      member.setHomeAddress(new Address("homeCity", "street", "10000"));

      member.getFavoriteFoods().add("치킨");
      member.getFavoriteFoods().add("족발");
      member.getFavoriteFoods().add("피자");

      member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
      member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

      em.persist(member);

      em.flush();
      em.clear();

      Member findMember = em.find(Member.class, member.getId());

      // 치킨 -> 한식
      findMember.getFavoriteFoods().remove("치킨");
      findMember.getFavoriteFoods().add("한식");

      // old1 -> newCity1
      findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "10000"));
      findMember.getAddressHistory().add(new AddressEntity("newCity1", "street", "10000"));

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}

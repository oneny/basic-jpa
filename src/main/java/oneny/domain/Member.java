package oneny.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
public class Member extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "MEMBER_ID")
  private Long id;
  private String username;

  // 기간
  @Embedded
  private Period workPeriod;

  // 주소
  @Embedded
  private Address homeAddress;

  @ElementCollection
  @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
  @Column(name = "FOOD_NAME") // String 하나이기 때문에 그 컬럼에 대해 이름을 정할 수 있음. List<Address> 같은 경우는 안됨
  private Set<String> favoriteFoods = new HashSet<>();

  /**
   * 값 타입 켈렉션의 제약사항
   * 값 타입은 엔티티와 다르게 식별자 개념이 없다.
   * 값은 변경하면 추적이 어렵다.
   * 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
   */
//  @ElementCollection
//  @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//  private List<Address> addressHistory = new ArrayList<>();

  /**
   * 실무에서 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려하는 것이 좋다.
   * 일대다 관계를 위한 엔티티(AddressEntity)를 만들고, 여기에서 값 타입을 사용한다.
   * 영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션처럼 사용
   */
  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "MEMBER_ID")
  private List<AddressEntity> addressHistory = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Period getWorkPeriod() {
    return workPeriod;
  }

  public void setWorkPeriod(Period workPeriod) {
    this.workPeriod = workPeriod;
  }

  public Address getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(Address homeAddress) {
    this.homeAddress = homeAddress;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public void changeCityOfAddress(String city) {
    homeAddress = homeAddress.changeCity(city);
  }

  public Set<String> getFavoriteFoods() {
    return favoriteFoods;
  }

  public void setFavoriteFoods(Set<String> favoriteFoods) {
    this.favoriteFoods = favoriteFoods;
  }

  public List<AddressEntity> getAddressHistory() {
    return addressHistory;
  }
}

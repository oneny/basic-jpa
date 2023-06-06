package oneny.jpql.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * NamedQuery
 * 미리 정의해서 이름을 부여해두고 사용하는 JPQL -> 정적 쿼리
 * 애플리케이션 로딩 시점에 초기화 후 재사용
 * 애플리케이션 로딩 시점에 쿼리를 검증할 수 있는 아주 좋은 장점이 있다.
 */
@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {

  @Id @GeneratedValue
  @Column(name = "MEMBER_ID")
  private Long id;

  private String username;

  private int age;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "TEAM_ID")
  private Team team;

  @Enumerated(EnumType.STRING)
  private MemberType type;

  public void setType(MemberType type) {
    this.type = type;
  }

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

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public Team getTeam() {
    return team;
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

  @Override
  public String toString() {
    return "Member{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", age=" + age +
            ", team=" + team + // team은 양방향으로 무한호출이 걸릴 위험있음
            '}';
  }
}

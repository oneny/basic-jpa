package oneny.jpql.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
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
//            ", team=" + team + // team은 양방향으로 무한호출이 걸릴 위험있음
            '}';
  }
}

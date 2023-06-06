package oneny.jpql.domain;

public class UserDTO {

  private String username;
  private int age;

  public UserDTO(String username, int age) {
    this.username = username;
    this.age = age;
  }

  @Override
  public String toString() {
    return "UserDTO{" +
            "name='" + username + '\'' +
            ", age=" + age +
            '}';
  }
}

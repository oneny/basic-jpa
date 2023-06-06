package oneny.jpql.dialect;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class H2Dialect extends org.hibernate.dialect.H2Dialect {
  public H2Dialect() {
    registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
  }
}

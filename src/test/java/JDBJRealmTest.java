import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JDBJRealmTest {

    DruidDataSource datasource = new DruidDataSource();
    {
        datasource.setUrl("jdbc:mysql://localhost:3306/world");
        datasource.setUsername("root");
        datasource.setPassword("123456");
    }

    @Test
    public void testAuthrnication(){

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(datasource);
        jdbcRealm.setPermissionsLookupEnabled(true);
        // 构建环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 主体提交
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("jack","123");
        subject.login(token);
        System.out.println(subject.isAuthenticated());
        subject.checkRole("admin");
//        subject.checkPermission("user.delete");
    }
}

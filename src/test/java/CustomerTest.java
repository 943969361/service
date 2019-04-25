import com.itheima.hchat.Realm.CuseomerRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomerTest {

    @Test
    public void testAuthrnication(){

        CuseomerRealm cuseomerRealm = new CuseomerRealm();
        // 构建环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(cuseomerRealm);

        // 加密
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        cuseomerRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        // 主体提交
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("jack","123456");
        subject.login(token);
        System.out.println(subject.isAuthenticated());
//        subject.checkRoles("admin","user");
//        subject.checkPermission("user:delete");
    }
}

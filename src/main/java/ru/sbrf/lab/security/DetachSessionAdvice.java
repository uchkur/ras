package ru.sbrf.lab.security;

import java.lang.reflect.Method;
        import java.sql.Connection;

//        import javax.inject.Inject;

import com.zaxxer.hikari.pool.HikariProxyConnection;
import oracle.jdbc.driver.OracleConnection;
import org.springframework.aop.MethodBeforeAdvice;

//import javax.inject.Inject;

public class DetachSessionAdvice implements MethodBeforeAdvice {

//    @Inject
    private OracleConnection proxyConnection;

    @Override
    public void before(final Method method, final Object[] args, final Object target) throws Throwable {
        if (method.getName().equals("close")) {
            System.out.println("ssssss");
//            statementMonitor.remove(Connection.class.cast(target));
        }
    }
}

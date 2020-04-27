package ru.sbrf.lab.datasource;

import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Objects;

public class RasConnection implements InvocationHandler {
    private Connection connection;
    RasConnection(Connection connection)
    {
        this.connection = Objects.requireNonNull(connection);
    }

    public static Connection newInstance (@NonNull Connection connection)
    {
        return (Connection) Proxy.newProxyInstance(RasConnection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                new RasConnection(connection));
    }
    @Override
    public Object invoke (Object proxy, Method method, Object[] params)  throws Throwable
    {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            }

            System.out.println("EXECUTED: " + method.getName());

            if ("close".equals(method.getName())) {
//todo session detach/destroy
                return method.invoke(connection, params);
            }
            else {
                return method.invoke(connection, params);
            }
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }
}

package ru.sbrf.lab.config.parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import ru.sbrf.lab.security.RestAuthenticationEntryPoint;
import ru.sbrf.lab.security.SudirAuthenticationProvider;
import ru.sbrf.lab.security.SudirUserDetails;


@Configuration
@EnableWebSecurity
@ComponentScan("ru.sbrf.lab.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    SudirAuthenticationProvider sudirAuthenticationProvider;
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(){
        super();
    }

    @Override
    protected void configure (final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(sudirAuthenticationProvider);
    }
    @Override
    protected void  configure (final HttpSecurity http) throws Exception
    {
        // @formatter:off
//        http
//                .authorizeRequests()
//                .antMatchers("/areYouOk").permitAll()
//                .antMatchers("/hello").permitAll()
//                .antMatchers("/graphiql").permitAll()
//                .antMatchers("/graphql").permitAll()
//                .antMatchers("/vendor/**").permitAll()
//                .antMatchers("/users").authenticated()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .csrf().disable()
//                .logout().disable();
        http
//        .sessionManagement()
        .anonymous()
//        .authorizeRequests().anyRequest()
//        .authenticated()
//        .and()
//        .httpBasic()
//        .authenticationEntryPoint(restAuthenticationEntryPoint);
        ;
        // @formatter:on
    }
}

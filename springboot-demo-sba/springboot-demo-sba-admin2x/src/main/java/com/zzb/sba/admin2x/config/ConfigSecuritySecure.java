package com.zzb.sba.admin2x.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

@Configuration
public class ConfigSecuritySecure extends WebSecurityConfigurerAdapter {
    private final AdminServerProperties adminServer;

    private final SecurityProperties security;

    public ConfigSecuritySecure(AdminServerProperties adminServer, SecurityProperties security) {
        this.adminServer = adminServer;
        this.security = security;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));
        http.authorizeRequests(
                (authorizeRequests) -> authorizeRequests
                        .antMatchers("/**/test.*", "/**/*.css", "/**/*.js", "/**/images/*").permitAll()
                        .antMatchers(this.adminServer.path("/assets/**")).permitAll()
                        .antMatchers(this.adminServer.path("/instances/**")).permitAll()
                        .antMatchers(this.adminServer.path("/actuator/info")).permitAll()
                        .antMatchers(this.adminServer.path("/actuator/health")).permitAll()
                        .antMatchers(this.adminServer.path("/test")).permitAll()
                        .antMatchers(this.adminServer.path("/arthas/**")).permitAll()
                        .antMatchers(this.adminServer.path("/login")).permitAll().anyRequest().authenticated()
        ).formLogin(
                (formLogin) -> formLogin.loginPage(this.adminServer.path("/login")).successHandler(successHandler).and()
        ).logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout"))).httpBasic(Customizer.withDefaults())
                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher(this.adminServer.path("/instances"),
                                        HttpMethod.POST.toString()),
                                new AntPathRequestMatcher(this.adminServer.path("/instances/*"),
                                        HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher(this.adminServer.path("/actuator/**"))
                        ))
                .rememberMe((rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));
    }

    // Required to provide UserDetailsService for "remember functionality"
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String userName = security.getUser().getName();
        String password = "{noop}" + security.getUser().getPassword();
        auth.inMemoryAuthentication().withUser(userName)
                .password(password).roles("USER");
    }


}
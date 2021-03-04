package org.springframework.samples.petclinic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "true")
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter tokenFilter;

    @Autowired
    public RestSecurityConfig(JwtTokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> {
            int errorCode = 401;
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(errorCode);
            httpServletResponse.getWriter().write("");
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable().formLogin().disable().httpBasic().disable()
            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
            .and().authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

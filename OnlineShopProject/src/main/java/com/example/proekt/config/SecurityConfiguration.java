package com.example.proekt.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) //so ova vo kontrolerot na post i get metodite moze da se pravi zastita @Secured("ROLE_ADMIN")

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(this.customUsernamePasswordAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      http
              .csrf().disable()
              .authorizeRequests()//site baranja da bidat avtorizirani
              .antMatchers("/" ,"/home" ,"/signup/**","/bootstrap/**" , "/images/**","/contact/**", "/aboutus")//ovie da bidat dozvoleni bez avtentikacija
              .permitAll()
              .anyRequest()
              .authenticated()//site baranja da bidat avtenticirani
              .and()
              .formLogin()
              .loginPage("/login").permitAll()// so ova ke koristi nasa login html
              .failureUrl("/login?error=BadCredentials") //dokolku ne e uspesna najavata
              .defaultSuccessUrl("/products", true) //dokolku e uspesna
              .and()
              .logout()
              .logoutUrl("/logout")
              .clearAuthentication(true)
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .logoutSuccessUrl("/login")//koga ke se odjavi korisnikot da go nosi na /login
              .and()
              .exceptionHandling().accessDeniedPage("/products?error=Nemate pristap.");




    }




}


package com.rnd.prs.springsec

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class App {

    val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/")
    fun welcome(): String {
        logger.info("Welcome to the security app: ")

        return "<h1>Welcome"
    }

    @GetMapping("/admin")
    fun adminOnlyCall(user: Principal): String {
        logger.info("adminOnlyCall", user.name)

        return "<h1>adminOnlyCall - ${user.name}</h1>"
    }

    @GetMapping("/user")
    fun userOnlyCall(user: Principal): String {
        logger.info("userOnlyCall", user.name)

        return "<h1>userOnlyCall - ${user.name}</h1>"
    }
}


@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(authBuilder: AuthenticationManagerBuilder) {
        authBuilder.inMemoryAuthentication().withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN")


    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin()


    }


    @Bean
    fun getPasswordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()
}


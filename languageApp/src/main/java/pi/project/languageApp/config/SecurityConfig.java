package pi.project.languageApp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        logger.info("Configuring UserDetailsManager...");
        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        theUserDetailsManager
                .setUsersByUsernameQuery("select username, password, enabled from users where username=?");
        theUserDetailsManager
                .setAuthoritiesByUsernameQuery("select username, authority from authorities where username=?");

        return theUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/css/**", "/js/**", "/images/**", "sharedElements/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/security/loginUser").permitAll()
                                .requestMatchers(HttpMethod.POST, "/security/manualLogout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/security/showFormCreateUser").permitAll()
                                .requestMatchers(HttpMethod.POST, "/security/saveUser").permitAll()
                                .requestMatchers(HttpMethod.GET, "/testing/list").hasRole("ADMIN")

                                //LANGUAGES
                                .requestMatchers(HttpMethod.GET, "/languages/list").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/languages/listSearch").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/languages/showFormForAddLanguage").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/languages/showFormForUpdateLanguage").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/languages/save").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/languages/delete").hasRole("ADMIN")

                                //.requestMatchers(HttpMethod.GET, "/users/list").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.GET, "/users/listSearch").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.GET, "/users/showFormForAddUser").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.GET, "/users/showFormForUpdateUser").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.POST, "/users/save").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.GET, "/users/delete").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.GET, "/users/showFormForAddAdmin").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.POST, "/users/saveAdmin").hasRole("ADMIN")
                )
                .formLogin(form ->
                        form
                                .loginPage("/security/loginUser")
                                .loginProcessingUrl("/security/security-login")
                                .successHandler(new CustomAuthenticationSuccessHandler())
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/security/manualLogout")
                        .logoutSuccessUrl("/security/loginUser?logoutManual=true") // the URL to redirect to after logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());
        // disable Cross Site Request Forgery (CSRF)
        http.csrf(csfr->csfr.disable());
        return http.build();
    }
}
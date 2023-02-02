package com.example.springbootaws.security;


import com.example.springbootaws.security.jwt.AuthEntryPointJwt;
import com.example.springbootaws.security.jwt.AuthTokenFilter;
import com.example.springbootaws.security.jwt.JwtUtils;
import com.example.springbootaws.security.oauth.CustomOAuth2User;
import com.example.springbootaws.security.oauth.CustomOAuth2UserService;
import com.example.springbootaws.security.services.UserDetailsServiceImpl;
import com.example.springbootaws.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    CustomOAuth2UserService oAuth2UserService;

    @Autowired
    JwtUtils jwtUtils;


    private static final String ADMIN = User.Role.ADMIN.toString();
    private static final String USER = User.Role.USER.toString();
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    private final String[] pathsWithoutAuthorization = {
            "/**auth/**",
            "/api/v1/auth/**",
            //swagger
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger/resources",
            "/swagger/resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger-ui/**",
            "/swagger-ui.html"};
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/visit/**").hasAnyAuthority(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/hello/**").hasAnyAuthority(ADMIN, USER)
                .requestMatchers("/**").permitAll().anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("http://localhost:3000/", true)
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        CustomOAuth2User oAuth2User = ((CustomOAuth2User) authentication.getPrincipal());
                        userDetailsService.processOAuthPostLogin(oAuth2User);
                        String jwtToken = jwtUtils.generateTokenFromUserEmail(oAuth2User.getEmail());
                        Cookie cookie = new Cookie("szop-portfolio", jwtToken);
                        cookie.setPath("/api");
                        cookie.setMaxAge(7 * 24 * 60 * 60);
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                        response.sendRedirect("http://localhost:3000/");
                    }
                });
        http.authenticationProvider(authenticationProvider(userDetailsService));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}

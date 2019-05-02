package com.example.springboot.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAuthorizationFilter extends BasicAuthenticationFilter implements Filter {

    private static String AUTH_HEADER = "Auth-Token";

    public MyAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilterInternal called!!!");
        val token = request.getHeader(AUTH_HEADER);
        if (token == null) {
            log.error("no auth-token");
            chain.doFilter(request, response);
            return;
        }

        val auth = validateToken(token);
        if (auth.isPresent()) {
            SecurityContextHolder.getContext().setAuthentication(auth.get());
        }
        chain.doFilter(request, response);
    }

    private Optional<Authentication> validateToken(String token) {
        val authority = AuthorityUtils.createAuthorityList("ROLE_USER");
        if (token.equals("secret")) {
            authority.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (!token.equals("passwd")) {
            log.error("validate failure");
            return Optional.empty();
        }
        val auth = new UsernamePasswordAuthenticationToken(token, token, authority);
        return Optional.of(auth);
    }
}

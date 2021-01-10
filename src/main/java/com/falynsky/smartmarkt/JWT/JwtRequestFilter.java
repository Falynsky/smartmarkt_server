package com.falynsky.smartmarkt.JWT;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static String REQUEST_HEADER_BEGIN = "Wave ";
    private final static int REQUEST_HEADER_BEGIN_LENGTH = REQUEST_HEADER_BEGIN.length();


    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Auth");

        String username = null;
        String jwtToken = null;

        boolean requestTokenHeaderStartsWithWave = requestTokenHeader != null && requestTokenHeader.startsWith(REQUEST_HEADER_BEGIN);
        if (requestTokenHeaderStartsWithWave) {
            jwtToken = requestTokenHeader.substring(REQUEST_HEADER_BEGIN_LENGTH);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with 'Wave ' String");
        }

        SecurityContext context = SecurityContextHolder.getContext();

        boolean usernameExists = username != null && !username.isEmpty();
        boolean contextHasAuthentication = context.getAuthentication() != null;

        if (usernameExists && !contextHasAuthentication) {

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            Boolean isTokenValidate = jwtTokenUtil.validateToken(jwtToken, userDetails);

            if (isTokenValidate) {

                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                UsernamePasswordAuthenticationToken usernamePasswordAuthToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);

                usernamePasswordAuthToken.setDetails(details);
                context.setAuthentication(usernamePasswordAuthToken);
            }
        }

        chain.doFilter(request, response);
    }

}
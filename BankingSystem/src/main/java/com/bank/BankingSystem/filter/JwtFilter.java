package com.bank.BankingSystem.filter;

import com.bank.BankingSystem.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String authHeader = req.getHeader("Authorization");

        if(req.getRequestURI().contains("/banking/users")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;

        }
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String username = jwtUtil.validateToken(token);
            if(username != null){
                servletRequest.setAttribute("username", username);
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write("Invalid or missing token");

    }
}

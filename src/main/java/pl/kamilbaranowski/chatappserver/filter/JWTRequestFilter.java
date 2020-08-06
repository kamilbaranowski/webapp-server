package pl.kamilbaranowski.chatappserver.filter;

import com.google.api.gax.rpc.PermissionDeniedException;
import com.google.api.gax.rpc.StatusCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            System.out.println("OPTIONS header");
        }

        else if (httpServletRequest.getRequestURI().contains("/users")) {
            final String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String jwt = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                System.out.println("JWT from filter: " + jwt);
                try {
                    FirebaseAuth.getInstance().verifyIdToken(jwt);
                } catch (FirebaseAuthException e) {
                    System.out.println("Bad token Error code: " + HttpStatus.FORBIDDEN.value());
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                }
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                System.out.println("No auth header: Error code: " + HttpStatus.FORBIDDEN.value());
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        System.out.println(httpServletResponse.getStatus());

    }

}

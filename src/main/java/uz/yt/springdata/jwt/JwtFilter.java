package uz.yt.springdata.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.yt.springdata.dto.UserInfoDTO;
import uz.yt.springdata.redis.UserSession;
import uz.yt.springdata.redis.UserSessionRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")){
            String token = auth.substring(7);
            String uuid = jwtUtil.getSubject(token);
            Optional<UserSession> userSession = userSessionRepository.findById(uuid);
            if (!userSession.isPresent()){
                SecurityContextHolder.getContext().setAuthentication(null);
            }

            String userInfoStr = userSession.get().getUserInfo();
            UserInfoDTO userInfoDTO = new ObjectMapper().readValue(userInfoStr, UserInfoDTO.class);


            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    userInfoDTO,
                    null,
                    userInfoDTO.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(userToken);

        }else {
            SecurityContextHolder.getContext().setAuthentication(null);
        }


        filterChain.doFilter(request, response);
    }
}

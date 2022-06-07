package uz.yt.springdata.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import uz.yt.springdata.dao.User;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.dto.UserLoginDto;
import uz.yt.springdata.helper.constants.AppResponseCode;
import uz.yt.springdata.helper.constants.AppResponseMessages;
import uz.yt.springdata.jwt.JwtUtil;
import uz.yt.springdata.redis.UserSession;
import uz.yt.springdata.redis.UserSessionRedisRepository;
import uz.yt.springdata.repository.AuthoritiesRepository;
import uz.yt.springdata.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSessionRedisRepository userSessionRedisRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByUsername(username);
        return user.orElse(null);
    }

    public ResponseDTO<String> getToken(UserLoginDto userLoginDto, HttpServletRequest request) {
        User user = loadUserByUsername(userLoginDto.getUsername());
        if (user == null) {
            return new ResponseDTO<>(false, AppResponseCode.NOT_FOUND, AppResponseMessages.NOT_FOUND, "username");
        }
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            return new ResponseDTO<>(false, AppResponseCode.VALIDATION_ERROR, AppResponseMessages.MISMATCH, "password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String uuid = sysGuid();
        String userString = toStringFromUser(user);
        UserSession userSessionForCaching = UserSession
                .builder()
                .id(uuid)
                .userInfo(userString)
                .build();
        userSessionRedisRepository.save(userSessionForCaching);
        String token = jwtUtil.generateToken(uuid);
        return new ResponseDTO<>(true, AppResponseCode.OK, AppResponseMessages.OK, token);
    }

    private String sysGuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    private String toStringFromUser(User user) {
        return new Gson().toJson(user);
    }
}
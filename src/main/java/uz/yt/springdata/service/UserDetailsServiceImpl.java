package uz.yt.springdata.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import uz.yt.springdata.dao.Authorities;
import uz.yt.springdata.dao.User;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.dto.UserDTO;
import uz.yt.springdata.dto.UserInfoDTO;
import uz.yt.springdata.helper.constants.AppResponseCode;
import uz.yt.springdata.helper.constants.AppResponseMessages;
import uz.yt.springdata.jwt.JwtUtil;
import uz.yt.springdata.redis.UserSession;
import uz.yt.springdata.redis.UserSessionRepository;
import uz.yt.springdata.repository.AuthoritiesRepository;
import uz.yt.springdata.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserInfoDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByUsername(username);

        List<Authorities> authorities = authoritiesRepository.findAllByUsername(username);
        Set<SimpleGrantedAuthority> permissions = authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .collect(Collectors.toSet());

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        user.ifPresent(u -> {
            userInfoDTO.setPermissions(permissions);
            userInfoDTO.setUsername(u.getUsername());
            userInfoDTO.setPassword(u.getPassword());
            userInfoDTO.setFirstName(u.getFirstName());
            userInfoDTO.setLastName(u.getLastName());
            userInfoDTO.setPhoneNumber(u.getPhoneNumber());
            userInfoDTO.setId(u.getId());
            userInfoDTO.setAccount(u.getAccount());
        });
        return userInfoDTO;
    }

    public ResponseDTO<String> getToken(UserDTO userInfoDTO, HttpServletRequest request){
        UserInfoDTO user = loadUserByUsername(userInfoDTO.getUsername());
        if (user.getUsername() == null){
            return new ResponseDTO<>(false, AppResponseCode.NOT_FOUND, AppResponseMessages.NOT_FOUND, "username");
        }
        if (!passwordEncoder.matches(userInfoDTO.getPassword(), user.getPassword())){
            return new ResponseDTO<>(false, AppResponseCode.VALIDATION_ERROR, AppResponseMessages.MISMATCH, "password");
        }

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        userToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(userToken);

        String uuid = sysGuid();
        UserSession userSession = UserSession
                .builder()
                .id(uuid)
                .userInfo(new Gson().toJson(user))
                .build();

        userSessionRepository.save(userSession);

        String token = jwtUtil.generateToken(uuid);

        return new ResponseDTO<>(true, AppResponseCode.OK, AppResponseMessages.OK, token);
    }

    private String sysGuid(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
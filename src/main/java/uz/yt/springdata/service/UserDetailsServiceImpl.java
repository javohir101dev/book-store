package uz.yt.springdata.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import uz.yt.springdata.dao.Authorities;
import uz.yt.springdata.dao.User;
import uz.yt.springdata.dto.UserInfoDTO;
import uz.yt.springdata.repository.AuthoritiesRepository;
import uz.yt.springdata.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    @Override
    public UserInfoDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByUsername(username);

        List<Authorities> authorities = authoritiesRepository.findAllByUsername(username);
        Set<GrantedAuthority> permissions = authorities.stream()
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
}
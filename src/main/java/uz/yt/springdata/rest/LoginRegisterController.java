package uz.yt.springdata.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.dto.UserDTO;
import uz.yt.springdata.dto.UserLoginDto;
import uz.yt.springdata.service.UserDetailsServiceImpl;
import uz.yt.springdata.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class LoginRegisterController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserService userService;

//    LOGIN USER
    @PostMapping("/login")
    public ResponseDTO<String> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        return userDetailsService.getToken(userLoginDto, request);
    }

    //    REGISTER/CREATE USER
    @PostMapping("/register")
    public ResponseDTO<UserDTO> register(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    }

}

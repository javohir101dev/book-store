package uz.yt.springdata.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.dto.UserDTO;
import uz.yt.springdata.service.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping
    public ResponseDTO<String> login(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userDetailsService.getToken(userDTO, request);
    }
}

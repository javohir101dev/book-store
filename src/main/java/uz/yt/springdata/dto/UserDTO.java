package uz.yt.springdata.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BigDecimal account;
    private String username;
    private String password;
}

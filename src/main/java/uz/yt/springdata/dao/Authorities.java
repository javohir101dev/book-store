package uz.yt.springdata.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Authorities")
@Data
public class Authorities {

    @Id
    @GeneratedValue(generator = "auth_seq")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "authority")
    private String authority;

}

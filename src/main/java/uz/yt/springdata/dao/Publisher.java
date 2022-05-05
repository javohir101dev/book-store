package uz.yt.springdata.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "publisher")
@Data
public class Publisher {

    @Id
    @GeneratedValue(generator = "publisher_id_seq")
    @SequenceGenerator(name = "publisher_id_seq", sequenceName = "publisher_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adres_id")
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    private List<Book> books;
}

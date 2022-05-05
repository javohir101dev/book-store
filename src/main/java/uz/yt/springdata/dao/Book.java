package uz.yt.springdata.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Book")
@NamedQuery(name = "Book.findByAuthorName",
    query = "select b from Book b where b.authorId.firstName like ?1" //JPQL - Java Persistence Query Language
)
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nameuz")
    private String nameUz;

    @Column(name = "nameru")
    private String nameRu;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "published_date")
    private Date publishedDate;

    @Column(name = "page_count")
    private Integer pageCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author authorId;

    @Column(name = "genre")
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    public Book(Integer id, String nameUz, BigDecimal cost, Author authorId, String genre) {
        this.id = id;
        this.nameUz = nameUz;
        this.cost = cost;
        this.authorId = authorId;
        this.genre = genre;
    }

}

package uz.yt.springdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Integer id;
    private String name;
    private BigDecimal cost;
    private String genre;
    private String publishedDate;
    private Integer pageCount;
    private AuthorDTO author;
    private PublisherDTO publisherDTO;

    public BookDTO(Integer id, String name, BigDecimal cost, String genre, String publishedDate, Integer pageCount) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.genre = genre;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
    }

    public String toString(){
        return String.format("%d-kitob: \nNomi: %s \nNarxi: %.2f \nJanri: %s \nMuallif: %s", id, name, cost, genre, author);
    }
}

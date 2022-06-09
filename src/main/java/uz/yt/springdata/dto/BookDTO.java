package uz.yt.springdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Integer id;
    @NotBlank(message = "Name is empty")
    @Digits(message = "Is not number", integer = 10, fraction = 2)
    private String name;
    @NotNull(message = "Cost is empty")
    @Min(value = 1000, message = "Less then 1 000 UZS")
    @Max(value = 500000,  message = "More then 500 000 UZS")
    private BigDecimal cost;
    @NotBlank(message = "Genre is empty")
    private String genre;
    @Pattern(regexp = "\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])",
            message = "PublishedDate is not valid date")    //regex for date
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

package uz.yt.springdata.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.yt.springdata.dto.BookDTO;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("{lang}")
public class BookResource {

    private final BookService bookService;

    @GetMapping("/get-all")
    public ResponseDTO<Page<BookDTO>> getAll(@PathVariable String lang, @RequestParam Integer size, @RequestParam Integer page){
        return bookService.getAllBooks(size, page);
    }

    @PostMapping("/add")
    public ResponseDTO<BookDTO> add(@RequestBody BookDTO bookDTO, @PathVariable String lang){
        return bookService.addNew(bookDTO);
    }

    @PutMapping("/update")
    public ResponseDTO<BookDTO> update(@RequestBody BookDTO bookDTO, @PathVariable String lang){
        return bookService.update(bookDTO);
    }

    @GetMapping("/get-by-author-name")
    public ResponseDTO<List<BookDTO>> getAllByAuthor(@RequestParam String authorName){
        return bookService.findByAuthorName(authorName);
    }
}

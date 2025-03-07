package uz.yt.springdata.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.yt.springdata.auth.UserRoles;
import uz.yt.springdata.dto.BookDTO;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.service.BookService;

import javax.validation.Valid;
import javax.xml.datatype.DatatypeConstants;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookResource {

    private final static Logger log = LoggerFactory.getLogger(BookResource.class);

    private final BookService bookService;

    @GetMapping
    public ResponseDTO<Page<BookDTO>> getAll(@RequestParam(defaultValue = "5") Integer size,
                                             @RequestParam(defaultValue = "0") Integer page){
        return bookService.getAllBooks(size, page);
    }

    @GetMapping("/by-param")
    public ResponseDTO<?> getAllWithParam(@RequestParam MultiValueMap<String, String> params){
        return bookService.getAllWithParam(params);
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyAuthority('BOOK:CREATE', 'ROLE_BOOK_MANAGER')")
    public ResponseDTO<BookDTO> add(@RequestBody @Valid BookDTO bookDTO){

        return bookService.addNew(bookDTO);
    }

    @PutMapping
    public ResponseDTO<BookDTO> update(@RequestBody BookDTO bookDTO){
        return bookService.update(bookDTO);
    }
}

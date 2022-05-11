package uz.yt.springdata.service;

import org.springframework.stereotype.Service;
import uz.yt.springdata.dto.BookDTO;
import uz.yt.springdata.dto.ResponseDTO;
import uz.yt.springdata.repository.BookRepository;

@Service
public class UserService {

    private final BookRepository bookRepository;

    public UserService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public ResponseDTO<BookDTO> addBook(BookDTO bookDTO) {


        return null;
    }
}

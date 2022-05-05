package uz.yt.springdata.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.yt.springdata.dao.Book;

import java.math.BigDecimal;
import java.util.*;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    long countAllByCost(BigDecimal cost);
    Page<Book> findAllByGenre(String genre, Pageable pageable);
    Set<Book> findAllByGenre(String genre);
    List<Book> findDistinctByNameUzAndNameRu(String nameUz, String nameRu);
    List<Book> findAllByCostLessThanOrderByNameUzAsc(BigDecimal cost);
    List<Book> findAllByNameUzIn(List<String> names);
    List<Book> findByAuthorName(String name, Pageable pageable);

    @Query(value = "select * from book b where b.nameuz like ?1", nativeQuery = true)
    List<Book> nameUz(String nameuz);

    @Query(value = "select * from book b where b.cost > :cost and b.nameuz like :nameuz",
            nativeQuery=true,
            countQuery = "select count(1) from book b where b.cost > :cost and b.nameuz like :nameuz")
    List<Book> getByCost(@Param(value = "cost") BigDecimal cost,
                         @Param(value = "nameuz") String name,
                         Pageable pageable);


}

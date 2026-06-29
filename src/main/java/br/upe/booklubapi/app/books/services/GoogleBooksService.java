package br.upe.booklubapi.app.books.services;

import br.upe.booklubapi.app.books.dtos.BookItem;
import br.upe.booklubapi.app.books.dtos.BookItemQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface GoogleBooksService {

    PagedModel<BookItem> searchBooks(BookItemQuery query, Pageable pageable);
    PagedModel<BookItem> getTrendingBooks(Pageable pageable);
    BookItem getBookById(String id);

}

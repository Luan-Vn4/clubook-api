package br.upe.booklubapi.infra.core.gateways.GoogleBooks;

import br.upe.booklubapi.app.books.dtos.BookSearchResponse;
import br.upe.booklubapi.app.books.dtos.BookVolume;
import br.upe.booklubapi.domain.books.exceptions.GoogleBooksException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GoogleBooksGateway {

    private final WebClient webClient;
    private final String apiKey;


    public GoogleBooksGateway(@Qualifier("googleBooksWebClient") WebClient webClient,
                              @Value("${google.books.api.key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }


    public Page<BookVolume> searchBooks(String query, Pageable pageable) {
        return searchBooks(query, pageable, null);
    }

    public Page<BookVolume> searchBooks(String query, Pageable pageable, String orderBy) {
        log.info("Searching books on Google Books API with query: {} (orderBy: {})", query, orderBy);

        int index = pageable.getPageNumber() * pageable.getPageSize();

        BookSearchResponse response =
                webClient.get()
                .uri(uriBuilder -> {
                        uriBuilder
                            .path("/volumes")
                            .queryParam("q", query)
                            .queryParam("printType", "books")
                            .queryParam("startIndex", index)
                            .queryParam("maxResults", pageable.getPageSize());
                        if (orderBy != null && !orderBy.isBlank()) {
                            uriBuilder.queryParam("orderBy", orderBy);
                        }
                        return uriBuilder
                            .queryParam("key", apiKey)
                            .build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BookSearchResponse.class)
                .doOnSuccess(answer -> log.info("Successfully retrieved books"))
                .doOnError(error -> log.error("Error retrieving books", error))
                .onErrorMap(ex -> new GoogleBooksException("Failed to fetch books from Google Books API", ex))
                .block();

        List<BookVolume> volumes = Optional.ofNullable(response)
                .map(BookSearchResponse::getItems)
                .orElse(Collections.emptyList());

        int totalItems = Optional.ofNullable(response)
                .map(BookSearchResponse::getTotalItems)
                .orElse(0);

        return new PageImpl<>(volumes, pageable, totalItems);
    }

    public BookVolume getBookById(String volume_id) {
        log.info("Fetching book with ID: {}", volume_id);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes/{volume_id}")
                        .queryParam("key", apiKey)
                        .build(volume_id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BookVolume.class)
                .doOnError(error -> log.error("Error fetching book by Volume ID", error))
                .onErrorMap(ex -> new GoogleBooksException("Failed to fetch book by Volume ID", ex))
                .block();
    }

}

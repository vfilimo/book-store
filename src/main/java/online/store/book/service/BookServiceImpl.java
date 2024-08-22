package online.store.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.BookDto;
import online.store.book.dto.CreateBookRequestDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.BookMapper;
import online.store.book.model.Book;
import online.store.book.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book bookEntity = bookMapper.toEntity(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(bookEntity));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id));
        bookMapper.updateBookFromDto(bookRequestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}

package online.store.book.service.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.BookDtoWithoutCategoryIds;
import online.store.book.dto.book.BookSearchParameters;
import online.store.book.dto.book.CreateBookRequestDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.BookMapper;
import online.store.book.model.Book;
import online.store.book.repository.book.BookRepository;
import online.store.book.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book bookEntity = bookMapper.toEntity(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(bookEntity));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookMapper.toDto(bookRepository.findAll(pageable));
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

    @Override
    public List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = specificationBuilder.build(bookSearchParameters);
        return bookMapper.toDto(bookRepository.findAll(bookSpecification, pageable));
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable) {
        return bookMapper.toDtoWithoutCategoryIds(
                bookRepository.findAllByCategoryId(id, pageable));
    }
}

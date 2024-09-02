package online.store.book.mapper;

import java.util.List;
import online.store.book.config.MapperConfig;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.CreateBookRequestDto;
import online.store.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> books);

    List<BookDto> toDto(Page<Book> books);

    Book toEntity(CreateBookRequestDto bookRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(CreateBookRequestDto bookRequestDto, @MappingTarget Book book);
}

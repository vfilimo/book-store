package online.store.book.mapper;

import online.store.book.config.MapperConfig;
import online.store.book.dto.BookDto;
import online.store.book.dto.CreateBookRequestDto;
import online.store.book.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookRequestDto);
}

package online.store.book.mapper;

import java.util.List;
import online.store.book.config.MapperConfig;
import online.store.book.dto.BookDto;
import online.store.book.dto.CreateBookRequestDto;
import online.store.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    List<BookDto> fromEntityListToDtoList(List<Book> books);

    Book toEntity(CreateBookRequestDto bookRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(CreateBookRequestDto bookRequestDto, @MappingTarget Book book);
}

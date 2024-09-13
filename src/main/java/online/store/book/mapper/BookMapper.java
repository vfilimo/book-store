package online.store.book.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import online.store.book.config.MapperConfig;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.BookDtoWithoutCategoryIds;
import online.store.book.dto.book.CreateBookRequestDto;
import online.store.book.model.Book;
import online.store.book.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> books);

    List<BookDto> toDto(Page<Book> books);

    List<BookDtoWithoutCategoryIds> toDtoWithoutCategoryIds(Page<Book> books);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> listCategories = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoryIds(listCategories);
    }

    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequestDto bookRequestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookRequestDto) {
        Set<Category> categories = bookRequestDto.getCategoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }

    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(CreateBookRequestDto bookRequestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
}

package online.store.book.mapper;

import online.store.book.config.MapperConfig;
import online.store.book.dto.category.CategoryDto;
import online.store.book.dto.category.CreteCategoryRequestDto;
import online.store.book.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreteCategoryRequestDto creteCategoryRequestDto);
}

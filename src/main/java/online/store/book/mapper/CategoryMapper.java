package online.store.book.mapper;

import java.util.List;
import online.store.book.config.MapperConfig;
import online.store.book.dto.category.CategoryDto;
import online.store.book.dto.category.CreteCategoryRequestDto;
import online.store.book.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);

    Category toEntity(CreteCategoryRequestDto creteCategoryRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(
            CreteCategoryRequestDto categoryRequestDto, @MappingTarget Category category);
}

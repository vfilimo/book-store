package online.store.book.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import online.store.book.dto.category.CategoryDto;
import online.store.book.dto.category.CreateCategoryRequestDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.CategoryMapper;
import online.store.book.model.Category;
import online.store.book.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    private CategoryDto categoryDto;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private Category category;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("some description");

        categoryDto = new CategoryDto(category.getId(), category.getName(),
                category.getDescription());

        createCategoryRequestDto = new CreateCategoryRequestDto(category.getName(),
                category.getDescription());

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findAll_shouldReturnCorrectNumberOfCategory() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(categoryPage)).thenReturn(List.of(categoryDto));

        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(categoryDto, actual.get(0));
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(categoryPage);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void findById_existingId_shouldReturnCorrectCategory() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actualCategoryDto = categoryService.findById(id);
        assertEquals(categoryDto, actualCategoryDto);
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void findById_notExistingId_shouldThrowExceptions() {
        Long id = 2L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoryService.findById(id));
        assertEquals("Can't find category with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void save_correctCategory_shouldReturnCorrectCategory() {
        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actualCategoryDto = categoryService.save(createCategoryRequestDto);
        assertEquals(categoryDto, actualCategoryDto);
        verify(categoryMapper, times(1)).toEntity(createCategoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_existingId_shouldReturnCorrectCategory() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(createCategoryRequestDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actualCategoryDto = categoryService.update(id, createCategoryRequestDto);
        assertEquals(categoryDto, actualCategoryDto);
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).updateCategoryFromDto(createCategoryRequestDto, category);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_notExistingId_shouldThrowException() {
        Long id = 2L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(id, createCategoryRequestDto));
        assertEquals("Can't find category with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteById_successful() {
        Long id = 1L;

        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteById(id);
        verify(categoryRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(categoryRepository);
    }
}

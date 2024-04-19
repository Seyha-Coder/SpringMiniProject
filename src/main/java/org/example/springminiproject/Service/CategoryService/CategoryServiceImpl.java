package org.example.springminiproject.Service.CategoryService;

import org.example.springminiproject.Model.AppUserModel.AppUser;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.Category.Category;
import org.example.springminiproject.Model.Category.CategoryRequest;
import org.example.springminiproject.Repository.AppUserRepository;
import org.example.springminiproject.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class  CategoryServiceImpl implements CategoryService {
    public final CategoryRepository categoryRepository;
    public final AppUserRepository appUserRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, AppUserRepository appUserRepository) {
        this.categoryRepository = categoryRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<Category> getAllCategory(Integer offset, Integer limit) {
        offset = (offset-1)*limit;
        return categoryRepository.getAllCategory(offset,limit);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.getCategoryById(id);
    }

    @Override
    public Category createCategory(CategoryRequest categoryRequest, UUID userId) {
//        categoryRequest.setUsersId(1);
        AppUserDTO appUserDTO = appUserRepository.findByEmail(String.valueOf(userId));

//        categoryRequest.setUserId(appUserDTO.getUserId());
        return categoryRepository.createCategory(categoryRequest, userId);
    }

    @Override
    public Category updateCategory(UUID id, CategoryRequest categoryRequest) {
        AppUserDTO appUserDTO =appUserRepository.findByEmail(String.valueOf(id));
        return categoryRepository.updateCategory(id,categoryRequest);
    }

    @Override
    public Category deleteCategory(UUID id) {
        return categoryRepository.deleteCategory(id);
    }
}

package org.example.springminiproject.Service.CategoryService;

import jakarta.validation.Valid;
import org.example.springminiproject.Exception.AllNotfoundException;
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
        Category response = categoryRepository.getCategoryById(id);
        System.out.println(response);
        if (response == null){
            throw new AllNotfoundException("The category id " + id + " has not been founded.");
        }
        return response;
    }

    @Override
    public Category createCategory(
            @Valid CategoryRequest categoryRequest,
            UUID userId
    ) {
        AppUserDTO appUserDTO = appUserRepository.findByEmail(String.valueOf(userId));
        return categoryRepository.createCategory(categoryRequest, userId);
    }

    @Override
    public Category updateCategory(UUID id, CategoryRequest categoryRequest) {
        getCategoryById(id);
        AppUserDTO appUserDTO =appUserRepository.findByEmail(String.valueOf(id));
        return categoryRepository.updateCategory(id,categoryRequest);
    }

    @Override
    public Category deleteCategory(UUID id) {
        getCategoryById(id);
        return categoryRepository.deleteCategory(id);
    }
}

package org.example.springminiproject.Controller;
import org.example.springminiproject.Model.AppUserModel.CustomUserDetails;
import org.example.springminiproject.Model.Category.Category;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import org.example.springminiproject.Model.Category.APIResponseCategory;
import org.example.springminiproject.Model.Category.CategoryRequest;
import org.example.springminiproject.Service.CategoryService.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    public final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public ResponseEntity<?> getAllCategory(
                                            @RequestParam(defaultValue = "1") Integer offset,
                                            @RequestParam(defaultValue = "5") Integer limit){
        List<Category> category = categoryService.getAllCategory(offset,limit);
        APIResponseCategory<?> response = new APIResponseCategory<>(
                "The category is get category all successfully",
                category,
                HttpStatus.OK,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id){
        org.example.springminiproject.Model.Category.Category category = categoryService.getCategoryById(id);
        APIResponseCategory<?> response = new APIResponseCategory<>(
                "The category is get category by id successfully",
                category,
                HttpStatus.OK,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        UUID userId = getUserUserOfCurrentUser();
//        System.out.println("uuu"+email);
        Category category = categoryService.createCategory(categoryRequest,userId);
        APIResponseCategory<?> response = new APIResponseCategory<>(
                "The category is create successfully ",
                category,
                HttpStatus.OK,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);

    }
    UUID getUserUserOfCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        System.out.println(userDetails.getAppUserDTO().getUserId());
        return userDetails.getAppUserDTO().getUserId();
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID id , @RequestBody CategoryRequest categoryRequest){
        Category category = categoryService.updateCategory(id,categoryRequest);
        APIResponseCategory<?> response = new APIResponseCategory<>(
                "The category is update successfully",
                category,
                HttpStatus.OK,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id){
        org.example.springminiproject.Model.Category.Category category = categoryService.deleteCategory(id);
        APIResponseCategory<?> response = new APIResponseCategory<>(
                "The category is delete successfully",
                null,
                HttpStatus.OK,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
}

package org.example.springminiproject.Repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.example.springminiproject.Exception.UuidTypeHandler;
import org.example.springminiproject.Model.Category.Category;
import org.example.springminiproject.Model.Category.CategoryRequest;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CategoryRepository {
    @Results(id= "category", value ={
            @Result(property = "categoryId",column = "category_id",jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
            @Result(property = "user",column = "user_id",jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class
                    , one = @One(select = "org.example.springminiproject.Repository.AppUserRepository.getUserById")

            )

    })
    @Select("""
    SELECT * FROM categories LIMIT #{limit} OFFSET #{offset};
    """)
    List<Category> getAllCategory(Integer offset, Integer limit);
    @Select("""
    
    SELECT * FROM categories
    where category_id = #{id}::uuid;
    """)
    @ResultMap("category")
    Category getCategoryById(UUID id);

    @Select("""
    INSERT INTO categories(name, description, user_id)
    values (#{Category.name},#{Category.description},#{userId}::uuid)
    RETURNING *;
    """)
    @ResultMap("category")
    @Result(property = "userId",column = "user_id",jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)

    Category createCategory(@Param("Category") CategoryRequest categoryRequest,UUID userId);

    @Select("""
    UPDATE categories
    SET name = #{Category.name},description= #{Category.description}
    WHERE category_id = #{id} 
    returning *;
    """)
    @ResultMap("category")
    Category updateCategory(@Param("id") UUID id,@Param("Category") CategoryRequest categoryRequest);

    @Select("""
    
    DELETE FROM categories
    WHERE category_id= #{id}::uuid;
    """)
    @ResultMap("category")
    Category deleteCategory(UUID id);
}

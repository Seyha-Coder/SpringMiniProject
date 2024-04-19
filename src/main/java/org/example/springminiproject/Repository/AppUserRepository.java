package org.example.springminiproject.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.*;

import org.apache.ibatis.type.JdbcType;
import org.example.springminiproject.Exception.UuidTypeHandler;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.AppUserModel.AppUserRequest;

import java.util.UUID;


@Mapper
public interface AppUserRepository {
    @Results(id = "AppUserMapping", value = {
            @Result(property = "profileImage", column = "profile_image"),
            @Result(property = "userId", column = "user_id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class)})

    @Select("""
            INSERT INTO users  VALUES (DEFAULT,DEFAULT,#{user.email} , #{user.password},#{user.profileImage}) RETURNING *
            """)
    AppUserDTO saveUser(@Param("user") AppUserRequest appUserRequest);

    @Select("""
            SELECT * FROM users WHERE email = #{email}
            """)
    @ResultMap("AppUserMapping")
    AppUserDTO findByEmail(@Param("email") String email);

    @Select("""
            UPDATE users SET password = #{password} WHERE user_id = #{userId} RETURNING *
            """)
    AppUserDTO updatePassword(String password, UUID userId);
    @Select("""
    
    SELECT  * FROM users
     WHERE user_id = #{id}::uuid;
    """)
    @ResultMap("AppUserMapping")
    AppUserDTO getUserById(@Param("id") UUID id);
}


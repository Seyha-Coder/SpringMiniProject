package org.example.springminiproject.Model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springminiproject.Model.AppUserModel.AppUser;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category{
    private UUID categoryId;
    private String name;
    private String description;
    private AppUserDTO user;

}

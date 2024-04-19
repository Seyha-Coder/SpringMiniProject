package org.example.springminiproject.Model.Expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springminiproject.Model.AppUserModel.AppUser;
import org.example.springminiproject.Model.Category.Category;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {

    //@Pattern(regexp = "//d+", message = "amount must be number not letter")
    private Float amount;

    @NotNull
    @NotBlank(message = "description must not be blank")
    private String description;
    @NotNull(message = "date must not be null or blanks")
    private Date date;
    @NotNull(message = "category id must not be null")
    private UUID categoryId;
}

package org.example.springminiproject.Model.Expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springminiproject.Model.AppUserModel.AppUser;
import org.example.springminiproject.Model.AppUserModel.AppUserDTO;
import org.example.springminiproject.Model.Category.Category;


import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    private UUID expenseId;
    private Double amount;
    private String description;
    private Date date;
    private AppUserDTO user;
    private Category category;
}

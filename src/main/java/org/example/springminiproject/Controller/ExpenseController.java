package org.example.springminiproject.Controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.springminiproject.Model.ApiResponse.ApiResponse;
import org.example.springminiproject.Model.AppUserModel.CustomUserDetails;
import org.example.springminiproject.Model.Expense.Expense;
import org.example.springminiproject.Model.Expense.ExpenseRequest;
import org.example.springminiproject.Service.ExpenseService.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Api/v1/expense")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseService expenseService;


    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<?> getAllExpense(
            @Positive(message = "Limit cannot be negative or zero") @RequestParam(defaultValue = "1") Integer offset,
            @Positive(message = "Limit cannot be negative or zero") @RequestParam(defaultValue = "3") Integer limit,
            @RequestParam(defaultValue = "expense_id") String shortBy,
            @RequestParam(defaultValue = "false") boolean orderBY
    ){
        List<Expense> expenses = expenseService.getAllExpense(offset, limit,shortBy,orderBY);
        ApiResponse<?> response = new ApiResponse<>(
                "successfully fetched data",
                HttpStatus.OK,
                200,
                expenses
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEventById(@PathVariable UUID id){
        Expense expense = expenseService.getValueById(id);
        ApiResponse<Expense> response = new ApiResponse<>(
                "Successfully get expense by id ",
                HttpStatus.OK,
                200,
                expense
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest){
        UUID userId = getUserUserOfCurrentUser();

        Expense expense = expenseService.insertExpense(expenseRequest, userId);
        ApiResponse<?> response = new ApiResponse<>(
                "Successfully create expense ",
                HttpStatus.CREATED,
                200,
                expense
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable UUID id, @Valid @RequestBody  ExpenseRequest expenseRequest){
        Expense expense = expenseService.updateExpenseById(id, expenseRequest);
        ApiResponse<?> response = new ApiResponse<>(
                "sucessfully update expense by id  ",
                HttpStatus.OK,
                200,
                expense
        );
        return ResponseEntity.ok(response);
    }

    UUID getUserUserOfCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        System.out.println(userDetails.getAppUserDTO().getUserId());
        return userDetails.getAppUserDTO().getUserId();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable UUID id){
        expenseService.deleteExpense(id);
        ApiResponse<?> response = new ApiResponse<>(
                "sucessfully delete expense by id ",
                HttpStatus.OK,
                200,
                null
        );
        return ResponseEntity.ok(response);
    }

}

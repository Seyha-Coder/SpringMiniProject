package org.example.springminiproject.Service.ExpenseService;

import org.example.springminiproject.Model.Expense.Expense;
import org.example.springminiproject.Model.Expense.ExpenseRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    List<Expense> getAllExpense(Integer offset, Integer limit,String shortBy, boolean orderBY);

    Expense getValueById(UUID expenseId);

    Expense insertExpense(ExpenseRequest expenseRequest, UUID userId);

    Expense updateExpenseById(UUID expenseId, ExpenseRequest expenseRequest);

    void deleteExpense(UUID expenseId);
}

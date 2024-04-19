package org.example.springminiproject.Service.ExpenseService;

import org.apache.ibatis.javassist.NotFoundException;
import org.example.springminiproject.Exception.AllNotfoundException;
import org.example.springminiproject.Model.Expense.Expense;
import org.example.springminiproject.Model.Expense.ExpenseRequest;
import org.example.springminiproject.Repository.ExpenseRepository;
import org.example.springminiproject.Service.CategoryService.CategoryService;
import org.example.springminiproject.Service.CategoryService.CategoryServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryServiceImpl categoryService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryServiceImpl categoryService) {
        this.expenseRepository = expenseRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Expense> getAllExpense(Integer offset, Integer limit, String shortBy, boolean orderBY) {
        String orderBy=orderBY?"DESC":"ASC";
        return expenseRepository.getAllExpense(offset,limit,shortBy,orderBY);
    }

    @Override
    public Expense getValueById(UUID id) {
        categoryService.getCategoryById(id);
        return expenseRepository.findExpenseById(id);
    }


    @Override
    public Expense insertExpense(ExpenseRequest expenseRequest, UUID userId) {
        categoryService.getCategoryById(expenseRequest.getCategoryId());
        return expenseRepository.insertExpense(expenseRequest, userId);
    }

    @Override
    public Expense updateExpenseById(UUID id, ExpenseRequest expenseRequest) {
        categoryService.getCategoryById(id);
        return expenseRepository.updateExpense(id, expenseRequest);
    }

    @Override
    public void deleteExpense(UUID id) {
        categoryService.getCategoryById(id);
        expenseRepository.deleteExpense(id);
    }


}

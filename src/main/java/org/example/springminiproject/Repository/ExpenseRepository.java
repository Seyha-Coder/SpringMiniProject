package org.example.springminiproject.Repository;


import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.example.springminiproject.Exception.UuidTypeHandler;
import org.example.springminiproject.Model.Expense.Expense;
import org.example.springminiproject.Model.Expense.ExpenseRequest;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ExpenseRepository {

    @Results(id="expenseMapping", value = {
            @Result(property = "expenseId", column = "expense_id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
            @Result(property = "user", column = "user_id", jdbcType = JdbcType.OTHER,typeHandler = UuidTypeHandler.class,
                    one = @One(select = "org.example.springminiproject.Repository.AppUserRepository.getUserById")),
            @Result(property = "category", column = "category_id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class,
                    one = @One(select = "org.example.springminiproject.Repository.CategoryRepository.getCategoryById"))
    })


    @Select("""
            SELECT * FROM expenses
             ORDER BY #{shortBy}, #{orderBY}
         LIMIT #{limit} OFFSET (#{offset}-1) * #{limit}
           """)
    List<Expense> getAllExpense(@Param("offset") Integer offset,@Param("limit") Integer limit,String shortBy, boolean orderBY);

    @Select("""
    select * from expenses
    where expense_id = #{id}::uuid;
    """)
    @ResultMap("expenseMapping")
    Expense findExpenseById(@Param("id") UUID id);


    @Select("""
     insert into expenses( amount, description, date,user_id, category_id)
     VALUES (#{expense.amount}, #{expense.description}, #{expense.date},#{userId}, #{expense.categoryId} )
     RETURNING *;
""")
    @ResultMap("expenseMapping")
    Expense insertExpense(@Param("expense") ExpenseRequest expenseRequest,@Param("userId") UUID userId);

    @Select("""
       update expenses set amount= #{expense.amount},
                           description= #{expense.description},
                           date= #{expense.date},
                           category_id= #{expense.categoryId}
       where expense_id=#{expenseId}  RETURNING *;
""")
    @ResultMap("expenseMapping")
    Expense updateExpense(UUID expenseId, @Param("expense") ExpenseRequest expenseRequest);

    @Delete("""
   delete from expenses 
   where expense_id = #{expenseId}::uuid;
""")
    void deleteExpense(@Param("expenseId") UUID expenseId);
}

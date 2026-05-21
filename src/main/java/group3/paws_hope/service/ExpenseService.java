package group3.paws_hope.service;

import group3.paws_hope.dto.req.ExpenseReq;
import group3.paws_hope.dto.res.ExpenseRes;
import group3.paws_hope.entity.Expense;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.ExpenseRepository;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public List<ExpenseRes> getAll() {
        return expenseRepository.findAll().stream()
                .map(ExpenseRes::toJson)
                .toList();
    }

    public ExpenseRes findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        return ExpenseRes.toJson(expense);
    }

    public List<ExpenseRes> getByCategory(String category) {
        return expenseRepository.findByCategory(
                        Expense.Category.valueOf(category)
                ).stream()
                .map(ExpenseRes::toJson)
                .toList();
    }

    public List<ExpenseRes> getByUserId(Long userId) {
        return expenseRepository.findByCreatedBy_UserId(userId)
                .stream()
                .map(ExpenseRes::toJson)
                .toList();
    }

    public List<ExpenseRes> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByExpenseDateBetween(startDate, endDate
                ).stream()
                .map(ExpenseRes::toJson)
                .toList();
    }

    public ExpenseRes create(ExpenseReq req) {
        try {
            Expense expense = new Expense();

            expense.setCategory(Expense.Category.valueOf(req.getCategory()));
            expense.setAmount(req.getAmount());
            expense.setDescription(req.getDescription());
            expense.setExpenseDate(req.getExpenseDate());
            expense.setReceiptImageUrl(req.getReceiptImageUrl());

            if (req.getCreatedBy() != null) {
                User creator = userRepository.findById(req.getCreatedBy())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                expense.setCreatedBy(creator);
            }
            return ExpenseRes.toJson(expenseRepository.save(expense));
        } catch (Exception e) {
            return null;
        }
    }

    public ExpenseRes update(Long id, ExpenseReq req) {
        try {
            Expense expense = expenseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Expense not found"));

            expense.setCategory(Expense.Category.valueOf(req.getCategory()));
            expense.setAmount(req.getAmount());
            expense.setDescription(req.getDescription());
            expense.setExpenseDate(req.getExpenseDate());
            expense.setReceiptImageUrl(req.getReceiptImageUrl());

            return ExpenseRes.toJson(expenseRepository.save(expense));
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }
}
package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ExpenseRes {
    private Long expenseId;
    private String category;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
    private String receiptImageUrl;
    private Long createdBy;
    private Timestamp createdAt;

    public static ExpenseRes toJson(Expense expense) {
        return new ExpenseRes(
                expense.getExpenseId(),
                expense.getCategory() != null ? expense.getCategory().name() : null,
                expense.getAmount(),
                expense.getDescription(),
                expense.getExpenseDate(),
                expense.getReceiptImageUrl(),
                expense.getCreatedBy() != null ? expense.getCreatedBy().getUserId() : null,
                expense.getCreatedAt()
        );
    }
}
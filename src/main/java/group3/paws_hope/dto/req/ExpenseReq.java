package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseReq {
    @NotNull(message = "Category cannot be null.")
    private String category;

    @NotNull(message = "Amount cannot be null.")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Expense date cannot be null.")
    private LocalDate expenseDate;

    private String receiptImageUrl;
    private Long createdBy;
}
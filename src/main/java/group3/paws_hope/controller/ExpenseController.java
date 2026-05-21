package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.ExpenseReq;
import group3.paws_hope.dto.res.ExpenseRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ExpenseRes>>> getAll() {
        return ResponseHandler.success(expenseService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ExpenseRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(expenseService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO<List<ExpenseRes>>> getByCategory(@PathVariable String category) {
        return ResponseHandler.success(expenseService.getByCategory(category), "Success");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<ExpenseRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(expenseService.getByUserId(userId), "Success");
    }

    @GetMapping("/date-range")
    public ResponseEntity<ResponseDTO<List<ExpenseRes>>> getByDateRange(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseHandler.success(expenseService.getByDateRange(startDate, endDate), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ExpenseRes>> create(@Valid @RequestBody ExpenseReq req) {
        ExpenseRes res = expenseService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Expense created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create expense failed");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ExpenseRes>> update(
            @PathVariable Long id, @Valid @RequestBody ExpenseReq req) {

        ExpenseRes res = expenseService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Expense updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update expense failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseHandler.success("Expense deleted successfully.", "Success");
    }
}
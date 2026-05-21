package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.ItemDonationReq;
import group3.paws_hope.dto.res.ItemDonationRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.ItemDonationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item_donations")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ItemDonationController {
    private final ItemDonationService itemDonationService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ItemDonationRes>>> getAll() {
        return ResponseHandler.success(itemDonationService.getAll(), "Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ItemDonationRes>> findById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(itemDonationService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<ItemDonationRes>>> getByUserId(@PathVariable Long userId) {
        return ResponseHandler.success(itemDonationService.getByUserId(userId), "Success");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDTO<List<ItemDonationRes>>> getByStatus(@PathVariable String status) {
        return ResponseHandler.success(itemDonationService.getByStatus(status), "Success");
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO<List<ItemDonationRes>>> getByCategory(@PathVariable String category) {
        return ResponseHandler.success(
                itemDonationService.getByCategory(category), "Success");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ItemDonationRes>> create(@Valid @RequestBody ItemDonationReq req) {
        ItemDonationRes res = itemDonationService.create(req);
        if (res != null) {
            return ResponseHandler.success(res, "Item donation created successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Create item donation failed");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ItemDonationRes>> update(
            @PathVariable Long id, @Valid @RequestBody ItemDonationReq req) {

        ItemDonationRes res = itemDonationService.update(id, req);
        if (res != null) {
            return ResponseHandler.success(res, "Item donation updated successfully.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update item donation failed");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<ItemDonationRes>> updateStatus(
            @PathVariable Long id, @RequestParam String status, @RequestParam(required = false) Long receivedBy) {

        ItemDonationRes res = itemDonationService.updateStatus(id, status, receivedBy);
        if (res != null) {
            return ResponseHandler.success(res, "Item donation status updated.");
        }
        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update status failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Long id) {
        itemDonationService.delete(id);
        return ResponseHandler.success("Item donation deleted successfully.", "Success");
    }
}
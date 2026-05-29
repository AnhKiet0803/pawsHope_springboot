package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.res.PetStatusLogRes;
import group3.paws_hope.service.PetStatusLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet_status_logs")
@AllArgsConstructor
public class PetStatusLogController {
    private final PetStatusLogService petStatusLogService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<PetStatusLogRes>>> getAll() {
        return ResponseHandler.success(petStatusLogService.getAll(), "Success");
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<ResponseDTO<List<PetStatusLogRes>>> getByPetId(@PathVariable Long petId) {
        return ResponseHandler.success(petStatusLogService.getByPetId(petId), "Success");
    }
}
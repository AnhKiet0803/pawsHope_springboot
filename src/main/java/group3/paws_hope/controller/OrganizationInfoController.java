package group3.paws_hope.controller;

import group3.paws_hope.common.ResponseHandler;
import group3.paws_hope.dto.common.ResponseDTO;
import group3.paws_hope.dto.req.OranizationInfoReq;
import group3.paws_hope.dto.res.OranizationInfoRes;
import group3.paws_hope.enums.StatusCode;
import group3.paws_hope.service.OrganizationInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization_info")
@AllArgsConstructor
public class OrganizationInfoController {

    private final OrganizationInfoService organizationInfoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<OranizationInfoRes>> getInfo() {

        OranizationInfoRes res = organizationInfoService.getInfo();

        if (res != null) {
            return ResponseHandler.success(res, "Success");
        }

        return ResponseHandler.error(StatusCode.NOT_FOUND, "Organization info not found");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<OranizationInfoRes>> saveOrUpdate(@Valid @RequestBody OranizationInfoReq req) {
        OranizationInfoRes res = organizationInfoService.saveOrUpdate(req);

        if (res != null) {
            return ResponseHandler.success(res, "Organization info updated successfully.");
        }

        return ResponseHandler.error(StatusCode.BAD_REQUEST, "Update failed");
    }
}
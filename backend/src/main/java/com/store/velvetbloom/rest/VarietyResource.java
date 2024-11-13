package com.store.velvetbloom.rest;

import com.store.velvetbloom.model.VarietyDTO;
import com.store.velvetbloom.service.VarietyService;
import com.store.velvetbloom.util.ReferencedException;
import com.store.velvetbloom.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/varieties", produces = MediaType.APPLICATION_JSON_VALUE)
public class VarietyResource {

    private final VarietyService varietyService;

    public VarietyResource(final VarietyService varietyService) {
        this.varietyService = varietyService;
    }

    @GetMapping
    public ResponseEntity<List<VarietyDTO>> getAllVarieties() {
        return ResponseEntity.ok(varietyService.findAll());
    }

    @GetMapping("/{varietyId}")
    public ResponseEntity<VarietyDTO> getVariety(
            @PathVariable(name = "varietyId") final UUID varietyId) {
        return ResponseEntity.ok(varietyService.get(varietyId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createVariety(@RequestBody @Valid final VarietyDTO varietyDTO) {
        final UUID createdVarietyId = varietyService.create(varietyDTO);
        return new ResponseEntity<>(createdVarietyId, HttpStatus.CREATED);
    }

    @PutMapping("/{varietyId}")
    public ResponseEntity<UUID> updateVariety(
            @PathVariable(name = "varietyId") final UUID varietyId,
            @RequestBody @Valid final VarietyDTO varietyDTO) {
        varietyService.update(varietyId, varietyDTO);
        return ResponseEntity.ok(varietyId);
    }

    @DeleteMapping("/{varietyId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVariety(
            @PathVariable(name = "varietyId") final UUID varietyId) {
        final ReferencedWarning referencedWarning = varietyService.getReferencedWarning(varietyId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        varietyService.delete(varietyId);
        return ResponseEntity.noContent().build();
    }

}

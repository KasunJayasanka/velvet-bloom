package com.store.velvetbloom.rest;

import com.store.velvetbloom.model.SelectedItemDTO;
import com.store.velvetbloom.service.SelectedItemService;
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
@RequestMapping(value = "/api/selectedItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class SelectedItemResource {

    private final SelectedItemService selectedItemService;

    public SelectedItemResource(final SelectedItemService selectedItemService) {
        this.selectedItemService = selectedItemService;
    }

    @GetMapping
    public ResponseEntity<List<SelectedItemDTO>> getAllSelectedItems() {
        return ResponseEntity.ok(selectedItemService.findAll());
    }

    @GetMapping("/{selectedItemId}")
    public ResponseEntity<SelectedItemDTO> getSelectedItem(
            @PathVariable(name = "selectedItemId") final UUID selectedItemId) {
        return ResponseEntity.ok(selectedItemService.get(selectedItemId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSelectedItem(
            @RequestBody @Valid final SelectedItemDTO selectedItemDTO) {
        final UUID createdSelectedItemId = selectedItemService.create(selectedItemDTO);
        return new ResponseEntity<>(createdSelectedItemId, HttpStatus.CREATED);
    }

    @PutMapping("/{selectedItemId}")
    public ResponseEntity<UUID> updateSelectedItem(
            @PathVariable(name = "selectedItemId") final UUID selectedItemId,
            @RequestBody @Valid final SelectedItemDTO selectedItemDTO) {
        selectedItemService.update(selectedItemId, selectedItemDTO);
        return ResponseEntity.ok(selectedItemId);
    }

    @DeleteMapping("/{selectedItemId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSelectedItem(
            @PathVariable(name = "selectedItemId") final UUID selectedItemId) {
        selectedItemService.delete(selectedItemId);
        return ResponseEntity.noContent().build();
    }

}

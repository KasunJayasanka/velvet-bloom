package com.store.velvetbloom.rest;

import com.store.velvetbloom.model.ColorDTO;
import com.store.velvetbloom.service.ColorService;
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
@RequestMapping(value = "/api/colors", produces = MediaType.APPLICATION_JSON_VALUE)
public class ColorResource {

    private final ColorService colorService;

    public ColorResource(final ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        return ResponseEntity.ok(colorService.findAll());
    }

    @GetMapping("/{colorId}")
    public ResponseEntity<ColorDTO> getColor(@PathVariable(name = "colorId") final UUID colorId) {
        return ResponseEntity.ok(colorService.get(colorId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createColor(@RequestBody @Valid final ColorDTO colorDTO) {
        final UUID createdColorId = colorService.create(colorDTO);
        return new ResponseEntity<>(createdColorId, HttpStatus.CREATED);
    }

    @PutMapping("/{colorId}")
    public ResponseEntity<UUID> updateColor(@PathVariable(name = "colorId") final UUID colorId,
            @RequestBody @Valid final ColorDTO colorDTO) {
        colorService.update(colorId, colorDTO);
        return ResponseEntity.ok(colorId);
    }

    @DeleteMapping("/{colorId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteColor(@PathVariable(name = "colorId") final UUID colorId) {
        colorService.delete(colorId);
        return ResponseEntity.noContent().build();
    }

}

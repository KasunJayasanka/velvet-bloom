package com.store.velvetbloom.rest;

import com.store.velvetbloom.model.OrdersDTO;
import com.store.velvetbloom.service.OrdersService;
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
@RequestMapping(value = "/api/orderss", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrdersResource {

    private final OrdersService ordersService;

    public OrdersResource(final OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<List<OrdersDTO>> getAllOrderss() {
        return ResponseEntity.ok(ordersService.findAll());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDTO> getOrders(@PathVariable(name = "orderId") final UUID orderId) {
        return ResponseEntity.ok(ordersService.get(orderId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createOrders(@RequestBody @Valid final OrdersDTO ordersDTO) {
        final UUID createdOrderId = ordersService.create(ordersDTO);
        return new ResponseEntity<>(createdOrderId, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<UUID> updateOrders(@PathVariable(name = "orderId") final UUID orderId,
            @RequestBody @Valid final OrdersDTO ordersDTO) {
        ordersService.update(orderId, ordersDTO);
        return ResponseEntity.ok(orderId);
    }

    @DeleteMapping("/{orderId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrders(@PathVariable(name = "orderId") final UUID orderId) {
        final ReferencedWarning referencedWarning = ordersService.getReferencedWarning(orderId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ordersService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

}

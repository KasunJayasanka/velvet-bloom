package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Cart;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.SelectedItem;
import com.store.velvetbloom.model.SelectedItemDTO;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.repository.SelectedItemRepository;
import com.store.velvetbloom.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SelectedItemService {

    private final SelectedItemRepository selectedItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public SelectedItemService(final SelectedItemRepository selectedItemRepository,
            final CartRepository cartRepository, final ProductRepository productRepository) {
        this.selectedItemRepository = selectedItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<SelectedItemDTO> findAll() {
        final List<SelectedItem> selectedItems = selectedItemRepository.findAll(Sort.by("selectedItemId"));
        return selectedItems.stream()
                .map(selectedItem -> mapToDTO(selectedItem, new SelectedItemDTO()))
                .toList();
    }

    public SelectedItemDTO get(final UUID selectedItemId) {
        return selectedItemRepository.findById(selectedItemId)
                .map(selectedItem -> mapToDTO(selectedItem, new SelectedItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SelectedItemDTO selectedItemDTO) {
        final SelectedItem selectedItem = new SelectedItem();
        mapToEntity(selectedItemDTO, selectedItem);
        return selectedItemRepository.save(selectedItem).getSelectedItemId();
    }

    public void update(final UUID selectedItemId, final SelectedItemDTO selectedItemDTO) {
        final SelectedItem selectedItem = selectedItemRepository.findById(selectedItemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(selectedItemDTO, selectedItem);
        selectedItemRepository.save(selectedItem);
    }

    public void delete(final UUID selectedItemId) {
        selectedItemRepository.deleteById(selectedItemId);
    }

    private SelectedItemDTO mapToDTO(final SelectedItem selectedItem,
            final SelectedItemDTO selectedItemDTO) {
        selectedItemDTO.setSelectedItemId(selectedItem.getSelectedItemId());
        selectedItemDTO.setQuantity(selectedItem.getQuantity());
        selectedItemDTO.setCart(selectedItem.getCart() == null ? null : selectedItem.getCart().getCartId());
        selectedItemDTO.setProduct(selectedItem.getProduct() == null ? null : selectedItem.getProduct().getProductId());
        return selectedItemDTO;
    }

    private SelectedItem mapToEntity(final SelectedItemDTO selectedItemDTO,
            final SelectedItem selectedItem) {
        selectedItem.setQuantity(selectedItemDTO.getQuantity());
        final Cart cart = selectedItemDTO.getCart() == null ? null : cartRepository.findById(selectedItemDTO.getCart())
                .orElseThrow(() -> new NotFoundException("cart not found"));
        selectedItem.setCart(cart);
        final Product product = selectedItemDTO.getProduct() == null ? null : productRepository.findById(selectedItemDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        selectedItem.setProduct(product);
        return selectedItem;
    }

}

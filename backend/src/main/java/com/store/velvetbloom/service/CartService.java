package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Cart;
import com.store.velvetbloom.domain.Customer;
import com.store.velvetbloom.domain.SelectedItem;
import com.store.velvetbloom.model.CartDTO;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.SelectedItemRepository;
import com.store.velvetbloom.util.NotFoundException;
import com.store.velvetbloom.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final SelectedItemRepository selectedItemRepository;

    public CartService(final CartRepository cartRepository,
            final CustomerRepository customerRepository,
            final SelectedItemRepository selectedItemRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.selectedItemRepository = selectedItemRepository;
    }

    public List<CartDTO> findAll() {
        final List<Cart> carts = cartRepository.findAll(Sort.by("cartId"));
        return carts.stream()
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .toList();
    }

    public CartDTO get(final Integer cartId) {
        return cartRepository.findById(cartId)
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CartDTO cartDTO) {
        final Cart cart = new Cart();
        mapToEntity(cartDTO, cart);
        return cartRepository.save(cart).getCartId();
    }

    public void update(final Integer cartId, final CartDTO cartDTO) {
        final Cart cart = cartRepository.findById(cartId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cartDTO, cart);
        cartRepository.save(cart);
    }

    public void delete(final Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    private CartDTO mapToDTO(final Cart cart, final CartDTO cartDTO) {
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setCustomer(cart.getCustomer() == null ? null : cart.getCustomer().getCustomerId());
        return cartDTO;
    }

    private Cart mapToEntity(final CartDTO cartDTO, final Cart cart) {
        final Customer customer = cartDTO.getCustomer() == null ? null : customerRepository.findById(cartDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        cart.setCustomer(customer);
        return cart;
    }

    public ReferencedWarning getReferencedWarning(final Integer cartId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Cart cart = cartRepository.findById(cartId)
                .orElseThrow(NotFoundException::new);
        final SelectedItem cartSelectedItem = selectedItemRepository.findFirstByCart(cart);
        if (cartSelectedItem != null) {
            referencedWarning.setKey("cart.selectedItem.cart.referenced");
            referencedWarning.addParam(cartSelectedItem.getSelectedItemId());
            return referencedWarning;
        }
        return null;
    }

}

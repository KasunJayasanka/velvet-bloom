package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Color;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.Variety;
import com.store.velvetbloom.model.VarietyDTO;
import com.store.velvetbloom.repository.ColorRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.repository.VarietyRepository;
import com.store.velvetbloom.util.NotFoundException;
import com.store.velvetbloom.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VarietyService {

    private final VarietyRepository varietyRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;

    public VarietyService(final VarietyRepository varietyRepository,
            final ProductRepository productRepository, final ColorRepository colorRepository) {
        this.varietyRepository = varietyRepository;
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
    }

    public List<VarietyDTO> findAll() {
        final List<Variety> varieties = varietyRepository.findAll(Sort.by("varietyId"));
        return varieties.stream()
                .map(variety -> mapToDTO(variety, new VarietyDTO()))
                .toList();
    }

    public VarietyDTO get(final UUID varietyId) {
        return varietyRepository.findById(varietyId)
                .map(variety -> mapToDTO(variety, new VarietyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final VarietyDTO varietyDTO) {
        final Variety variety = new Variety();
        mapToEntity(varietyDTO, variety);
        return varietyRepository.save(variety).getVarietyId();
    }

    public void update(final UUID varietyId, final VarietyDTO varietyDTO) {
        final Variety variety = varietyRepository.findById(varietyId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(varietyDTO, variety);
        varietyRepository.save(variety);
    }

    public void delete(final UUID varietyId) {
        varietyRepository.deleteById(varietyId);
    }

    private VarietyDTO mapToDTO(final Variety variety, final VarietyDTO varietyDTO) {
        varietyDTO.setVarietyId(variety.getVarietyId());
        varietyDTO.setSize(variety.getSize());
        varietyDTO.setTotalAvailableCount(variety.getTotalAvailableCount());
        varietyDTO.setAvailability(variety.getAvailability());
        varietyDTO.setReorderCount(variety.getReorderCount());
        varietyDTO.setProduct(variety.getProduct() == null ? null : variety.getProduct().getProductId());
        return varietyDTO;
    }

    private Variety mapToEntity(final VarietyDTO varietyDTO, final Variety variety) {
        variety.setSize(varietyDTO.getSize());
        variety.setTotalAvailableCount(varietyDTO.getTotalAvailableCount());
        variety.setAvailability(varietyDTO.getAvailability());
        variety.setReorderCount(varietyDTO.getReorderCount());
        final Product product = varietyDTO.getProduct() == null ? null : productRepository.findById(varietyDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        variety.setProduct(product);
        return variety;
    }

    public ReferencedWarning getReferencedWarning(final UUID varietyId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Variety variety = varietyRepository.findById(varietyId)
                .orElseThrow(NotFoundException::new);
        final Color varietyColor = colorRepository.findFirstByVariety(variety);
        if (varietyColor != null) {
            referencedWarning.setKey("variety.color.variety.referenced");
            referencedWarning.addParam(varietyColor.getColorId());
            return referencedWarning;
        }
        return null;
    }

}

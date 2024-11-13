package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Color;
import com.store.velvetbloom.domain.Variety;
import com.store.velvetbloom.model.ColorDTO;
import com.store.velvetbloom.repository.ColorRepository;
import com.store.velvetbloom.repository.VarietyRepository;
import com.store.velvetbloom.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ColorService {

    private final ColorRepository colorRepository;
    private final VarietyRepository varietyRepository;

    public ColorService(final ColorRepository colorRepository,
            final VarietyRepository varietyRepository) {
        this.colorRepository = colorRepository;
        this.varietyRepository = varietyRepository;
    }

    public List<ColorDTO> findAll() {
        final List<Color> colors = colorRepository.findAll(Sort.by("colorId"));
        return colors.stream()
                .map(color -> mapToDTO(color, new ColorDTO()))
                .toList();
    }

    public ColorDTO get(final UUID colorId) {
        return colorRepository.findById(colorId)
                .map(color -> mapToDTO(color, new ColorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ColorDTO colorDTO) {
        final Color color = new Color();
        mapToEntity(colorDTO, color);
        return colorRepository.save(color).getColorId();
    }

    public void update(final UUID colorId, final ColorDTO colorDTO) {
        final Color color = colorRepository.findById(colorId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(colorDTO, color);
        colorRepository.save(color);
    }

    public void delete(final UUID colorId) {
        colorRepository.deleteById(colorId);
    }

    private ColorDTO mapToDTO(final Color color, final ColorDTO colorDTO) {
        colorDTO.setColorId(color.getColorId());
        colorDTO.setColorName(color.getColorName());
        colorDTO.setCount(color.getCount());
        colorDTO.setImgUrl(color.getImgUrl());
        colorDTO.setVariety(color.getVariety() == null ? null : color.getVariety().getVarietyId());
        return colorDTO;
    }

    private Color mapToEntity(final ColorDTO colorDTO, final Color color) {
        color.setColorName(colorDTO.getColorName());
        color.setCount(colorDTO.getCount());
        color.setImgUrl(colorDTO.getImgUrl());
        final Variety variety = colorDTO.getVariety() == null ? null : varietyRepository.findById(colorDTO.getVariety())
                .orElseThrow(() -> new NotFoundException("variety not found"));
        color.setVariety(variety);
        return color;
    }

}

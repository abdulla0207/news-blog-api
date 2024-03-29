package com.company.service;

import com.company.dto.RegionByLanguageDTO;
import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.enums.LanguageEnum;
import com.company.exception.ItemNotFoundException;
import com.company.exception.RegionCreateException;
import com.company.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RegionService {
    private final RegionRepository regionRepository;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public RegionService(RegionRepository regionRepository, ResourceMessageService resourceMessageService){
        this.regionRepository = regionRepository;
        this.resourceMessageService = resourceMessageService;
    }
    public RegionDTO create(RegionDTO regionDTO) {

        RegionEntity entity = mapToEntity(regionDTO);

        // Recreating requested RegionDTO, and adding the id field that was created in entity class,
        // because initially regionDTO id was 0
        RegionDTO response = new RegionDTO(entity.getId(),entity.getKey(),entity.getNameUz(),
                entity.getNameEn(),entity.isVisible(), entity.getCreatedAt(),entity.getUpdatedAt());

        regionRepository.save(entity);
        return response;
    }

    private RegionEntity mapToEntity(RegionDTO regionDTO) {
        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setVisible(regionDTO.visible());
        regionEntity.setKey(regionDTO.key());
        regionEntity.setNameUz(regionDTO.nameUz());
        regionEntity.setNameEn(regionDTO.nameEn());
        regionEntity.setCreatedAt(LocalDateTime.now());

        return regionEntity;
    }

    public RegionDTO updateById(int id, RegionDTO regionDTO, String lang) {
        Optional<RegionEntity> byId = regionRepository.findById(id);

        if(byId.isEmpty()) {
            log.warn("Region not found {}", id);
            throw new ItemNotFoundException(resourceMessageService.getMessage("region.not.found", lang));
        }

        RegionEntity regionEntity = byId.get();

        regionEntity.setUpdatedAt(LocalDateTime.now());
        regionEntity.setVisible(regionDTO.visible());
        regionEntity.setKey(regionDTO.key());
        regionEntity.setNameUz(regionDTO.nameUz());
        regionEntity.setNameEn(regionDTO.nameEn());

        regionRepository.save(regionEntity);

        return regionDTO;
    }

    public String deleteById(int id, String lang) {
        Optional<RegionEntity> byId = regionRepository.findById(id);
        if(byId.isEmpty()) {
            log.warn("Region not found {}", id);
            throw new ItemNotFoundException(resourceMessageService.getMessage("region.not.found", lang));
        }

        regionRepository.deleteById(id);
        return resourceMessageService.getMessage("region.deleted", lang);
    }

    public List<RegionByLanguageDTO> getListByLanguage(String languageEnum) {
        List<RegionEntity> allWhereVisibleIsTrue = regionRepository.findAllWhereVisibleIsTrue();

        List<RegionByLanguageDTO> responseList = new ArrayList<>();
        allWhereVisibleIsTrue.forEach(regionEntity -> {
            String name = null;
            switch (languageEnum){
                case "uz" -> name = regionEntity.getNameUz();
                case "en" -> name = regionEntity.getNameEn();
                default -> {
                    log.warn("Wrong language");
                    throw new IllegalArgumentException("Wrong language");
                }
            }
            RegionByLanguageDTO region = new RegionByLanguageDTO(regionEntity.getId(), regionEntity.getKey(), name);
            responseList.add(region);
        });

        return responseList;
    }

    public List<RegionDTO> getList() {
        List<RegionEntity> all = regionRepository.findAll();

        List<RegionDTO> response = toDtoList(all);

        return response;
    }

    private List<RegionDTO> toDtoList(List<RegionEntity> all) {
        List<RegionDTO> responseList = new ArrayList<>();
        all.forEach(regionEntity -> {
            RegionDTO regionDTO = new RegionDTO(regionEntity.getId(), regionEntity.getKey(), regionEntity.getNameUz(),
                    regionEntity.getNameEn(), regionEntity.isVisible(), regionEntity.getCreatedAt(), regionEntity.getUpdatedAt());
            responseList.add(regionDTO);
        });
        return responseList;
    }
}

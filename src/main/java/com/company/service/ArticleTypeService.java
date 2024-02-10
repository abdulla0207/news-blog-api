package com.company.service;

import com.company.dto.ArticleTypeCreateDTO;
import com.company.dto.ArticleTypeDTO;
import com.company.dto.ArticleTypeByLanguageDTO;
import com.company.entity.ArticleTypeEntity;
import com.company.enums.LanguageEnum;
import com.company.exception.ArticleTypeCreationException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArticleTypeService {
    private final ArticleTypeRepository articleTypeRepository;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public ArticleTypeService(ArticleTypeRepository articleTypeRepository, ResourceMessageService resourceMessageService){
        this.articleTypeRepository = articleTypeRepository;
        this.resourceMessageService = resourceMessageService;
    }

    public ArticleTypeDTO create(ArticleTypeCreateDTO articleTypeDTO) {

        ArticleTypeEntity entity = toEntity(articleTypeDTO);

        articleTypeRepository.save(entity);

        return toDTO(entity);
    }

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity){
        ArticleTypeDTO dto = new ArticleTypeDTO(entity.getId(), entity.getKey(), entity.getNameUz(), entity.getNameEn(),
                entity.isVisible(), entity.getCreatedAt(), entity.getUpdatedAt());
        return dto;
    }

    private ArticleTypeEntity toEntity(ArticleTypeCreateDTO articleTypeDTO) {
        ArticleTypeEntity articleTypeEntity = new ArticleTypeEntity();
        articleTypeEntity.setCreatedAt(LocalDateTime.now());
        articleTypeEntity.setNameUz(articleTypeDTO.nameUz());
        articleTypeEntity.setNameEn(articleTypeDTO.nameEn());
        articleTypeEntity.setKey(articleTypeDTO.key());
        articleTypeEntity.setVisible(true);

        return articleTypeEntity;
    }

    private void checkDTO(ArticleTypeDTO articleTypeDTO) {
        if(articleTypeDTO.key().isEmpty() || articleTypeDTO.key().isBlank()){
            throw new ArticleTypeCreationException("Article Type Key should be filled");
        }
        if(articleTypeDTO.nameUz().isEmpty() || articleTypeDTO.nameUz().isBlank())
            throw new ArticleTypeCreationException("Article Type Name UZ should be filled");
        if(articleTypeDTO.nameEn().isEmpty() || articleTypeDTO.nameEn().isBlank())
            throw new ArticleTypeCreationException("Article Type Name ENG should be filled");

    }

    public ArticleTypeDTO updateById(int id, ArticleTypeDTO dto, String lang) {
        Optional<ArticleTypeEntity> byId = articleTypeRepository.findById(id);

        if(byId.isEmpty()) {
            log.warn("Article Type not found {}", id);
            throw new ItemNotFoundException(resourceMessageService.getMessage("article.type.not.found", lang));
        }

        ArticleTypeEntity entity = byId.get();

        checkDTO(dto);

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setKey(dto.key());
        entity.setNameEn(dto.nameEn());
        entity.setNameUz(dto.nameUz());
        entity.setVisible(dto.visible());

        articleTypeRepository.save(entity);

        return dto;
    }

    public Page<ArticleTypeDTO> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleTypeEntity> entities = articleTypeRepository.findAll(pageable);

        List<ArticleTypeEntity> list = entities.getContent();
        long totalElements = entities.getTotalElements();

        List<ArticleTypeDTO> dtoList = toDtoList(list);

        Page<ArticleTypeDTO> response = new PageImpl<>(dtoList, pageable, totalElements);
        return response;
    }

    private List<ArticleTypeDTO> toDtoList(List<ArticleTypeEntity> list) {
        List<ArticleTypeDTO> dtos = new ArrayList<>();
        list.forEach(entity -> {
            ArticleTypeDTO dto = new ArticleTypeDTO(entity.getId(), entity.getKey(), entity.getNameUz(),
                    entity.getNameEn(),entity.isVisible(),entity.getCreatedAt(),entity.getUpdatedAt());

            dtos.add(dto);
        });

        return dtos;
    }

    public String deleteById(int id, String lang) {
        Optional<ArticleTypeEntity> byId = articleTypeRepository.findById(id);

        if(byId.isEmpty()) {
            log.warn("Article Type not found {}", id);
            throw new ItemNotFoundException(resourceMessageService.getMessage("article.type.not.found", lang));
        }

        articleTypeRepository.deleteById(id);

        return resourceMessageService.getMessage("article.type.deleted", lang);
    }

    public List<ArticleTypeByLanguageDTO> getListByLanguage(LanguageEnum languageEnum) {
        List<ArticleTypeEntity> all = articleTypeRepository.findAllWhereVisibleIsTrue();

        List<ArticleTypeByLanguageDTO> response = new ArrayList<>();
        all.forEach(entity -> {
            String name = entity.getNameEn();
            switch (languageEnum){
                case UZ -> name = entity.getNameUz();
                case EN -> name = entity.getNameEn();
            }
            ArticleTypeByLanguageDTO dto = new ArticleTypeByLanguageDTO(entity.getId(), entity.getKey(), name);
            response.add(dto);
        });

        return response;
    }
}

package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleTypeDTO;
import com.company.entity.ArticleTypeEntity;
import com.company.exception.ArticleTypeCreationException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleTypeRepository;
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

@Service
public class ArticleTypeService {
    private final ArticleTypeRepository articleTypeRepository;

    @Autowired
    public ArticleTypeService(ArticleTypeRepository articleTypeRepository){
        this.articleTypeRepository = articleTypeRepository;
    }

    public ArticleTypeDTO create(ArticleTypeDTO articleTypeDTO) {
        checkDTO(articleTypeDTO);

        ArticleTypeEntity entity = toEntity(articleTypeDTO);

        articleTypeRepository.save(entity);

        return toDTO(entity);
    }

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity){
        ArticleTypeDTO dto = new ArticleTypeDTO(entity.getId(), entity.getKey(), entity.getNameUz(), entity.getNameEn(),
                entity.isVisible(), entity.getCreatedAt(), entity.getUpdatedAt());
        return dto;
    }

    private ArticleTypeEntity toEntity(ArticleTypeDTO articleTypeDTO) {
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

    public ArticleTypeDTO updateById(int id, ArticleTypeDTO dto) {
        Optional<ArticleTypeEntity> byId = articleTypeRepository.findById(id);

        if(byId.isEmpty())
            throw new ItemNotFoundException("Article Type with id " + id + " not found");

        ArticleTypeEntity entity = byId.get();

        checkDTO(dto);

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setKey(dto.key());
        entity.setNameEn(dto.nameEn());
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
}

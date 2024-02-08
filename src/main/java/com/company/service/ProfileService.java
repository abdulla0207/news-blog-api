package com.company.service;

import com.company.dto.JwtDTO;
import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.company.exception.AppForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.exception.ProfileCreateException;
import com.company.exception.TokenNotValidException;
import com.company.repository.ProfileRepository;
import com.company.util.MD5Util;
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
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, ResourceMessageService resourceMessageService) {
        this.profileRepository = profileRepository;
        this.resourceMessageService = resourceMessageService;
    }

    public ProfileDTO create(ProfileDTO profileDTO, int adminId, String lang) {
        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(profileDTO.email());
        if (byEmail.isPresent()) {
            log.warn("User with email already exists {}", profileDTO.email());
            throw new ProfileCreateException(resourceMessageService.getMessage("user.email.exist", lang));
        }
        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneNumber(profileDTO.phoneNumber());
        if (byPhoneNumber.isPresent()) {
            log.warn("User with phone number already exists {}", profileDTO.phoneNumber());
            throw new ProfileCreateException(resourceMessageService.getMessage("user.phone.exist", lang));
        }
        ProfileEntity entity = toEntity(profileDTO);
        entity.setParentId(adminId);
        entity.setRole(profileDTO.roleEnum());
        profileRepository.save(entity);
        return profileDTO;
    }

    public Page<ProfileDTO> getProfileList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProfileEntity> all = profileRepository.findAll(pageable);

        long totalElements = all.getTotalElements();
        List<ProfileEntity> content = all.getContent();

        List<ProfileDTO> profileDTOS = toDTOList(content);

        PageImpl<ProfileDTO> response = new PageImpl<>(profileDTOS, pageable, totalElements);

        return response;
    }

    private List<ProfileDTO> toDTOList(List<ProfileEntity> entityList) {
        List<ProfileDTO> result = new ArrayList<>();

        entityList.forEach(profileEntity -> {
            final ProfileDTO profileDTO = toDTO(profileEntity);

            result.add(profileDTO);
        });
        return result;
    }

    private ProfileDTO toDTO(ProfileEntity entity){
        ProfileDTO dto = new ProfileDTO(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getPassword(),
                entity.getStatus(),
                entity.getRole(),
                entity.getCreatedAt()
        );
        return dto;
    }

    private ProfileEntity toEntity(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setSurname(profileDTO.surname());
        profileEntity.setName(profileDTO.name());
        profileEntity.setPassword(MD5Util.encode(profileDTO.password()));
        profileEntity.setEmail(profileDTO.email());
        profileEntity.setRole(profileDTO.roleEnum());
        profileEntity.setStatus(ProfileStatusEnum.ACTIVE);
        profileEntity.setCreatedAt(LocalDateTime.now());
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setPhoneNumber(profileDTO.phoneNumber());
        profileEntity.setVisible(true);
        return profileEntity;
    }

    public String deleteById(int id, String lang) {
        Optional<ProfileEntity> byId = profileRepository.findById(id);

        if (byId.isEmpty()) {
            log.warn("Profile not found {}", id);
            throw new ItemNotFoundException(resourceMessageService.getMessage("user.not.found", lang));
        }
        profileRepository.deleteById(id);
        return resourceMessageService.getMessage("user.deleted", lang);
    }

    private ProfileEntity getById(int id) {
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        return byId.orElse(null);
    }

    public String update(ProfileDTO profileDTO, int id, String lang) {
        int b = profileRepository.updateByAdminByProfileId(profileDTO.name(), profileDTO.surname(), profileDTO.roleEnum(), profileDTO.statusEnum(), id);

        if (b <= 0) {
            log.warn("Profile not updated");
            throw new RuntimeException(resourceMessageService.getMessage("user.not.updated", lang));
        }
        return resourceMessageService.getMessage("user.updated", lang);
    }

    public String updateByProfile(ProfileDTO profileDTO, int tokenId, String lang) {

        int b = profileRepository.updateByAll(profileDTO.name(), profileDTO.surname(), tokenId);

        if(b <=0) {
            log.warn("Profile not updated");
            throw new RuntimeException(resourceMessageService.getMessage("user.not.updated", lang));
        }

        return resourceMessageService.getMessage("user.updated", lang);
    }
}

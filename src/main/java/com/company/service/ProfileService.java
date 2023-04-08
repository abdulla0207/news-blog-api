package com.company.service;

import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.company.exception.ItemNotFoundException;
import com.company.exception.ProfileCreateException;
import com.company.repository.ProfileRepository;
import com.company.util.MD5Util;
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
public class ProfileService {
    private ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public ProfileDTO create(ProfileDTO profileDTO) {
        //Validation
        if(profileDTO.getName().isEmpty() || profileDTO.getName().isBlank())
            throw new ProfileCreateException("Name field is empty");
        if(profileDTO.getSurname().isBlank() || profileDTO.getSurname().isEmpty())
            throw new ProfileCreateException("Surname field is empty");
        if(!profileDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"))
            throw new ProfileCreateException("Password should have at least one number, one lowercase or uppercase character" +
                    " and the length should be at least 8 letters");
        if(!profileDTO.getPhoneNumber().matches("[+]998[0-9]{9}"))
            throw new ProfileCreateException("Phone number should be in the following format: +998 xx xxx-xx-xx");

        profileRepository.save(toEntity(profileDTO));
        return profileDTO;
    }

    public Page<ProfileDTO> getProfileList(int page){
        Pageable pageable = PageRequest.of(page, 5);
        Page<ProfileEntity> all = profileRepository.findAll(pageable);

        long totalElements = all.getTotalElements();
        List<ProfileEntity> content = all.getContent();

        List<ProfileDTO> profileDTOS = toDTOList(content);

        PageImpl<ProfileDTO> response = new PageImpl<>(profileDTOS, pageable, totalElements);

        return response;
    }

    private List<ProfileDTO> toDTOList(List<ProfileEntity> entityList){
        List<ProfileDTO> result = new ArrayList<>();
        ProfileDTO profileDTO = new ProfileDTO();

        entityList.forEach(profileEntity -> {
            profileDTO.setRole(profileEntity.getRole());
            profileDTO.setEmail(profileEntity.getEmail());
            profileDTO.setName(profileEntity.getName());
            profileDTO.setStatus(profileEntity.getStatus());
            profileDTO.setSurname(profileEntity.getSurname());
            profileDTO.setPhoneNumber(profileEntity.getPhoneNumber());

            result.add(profileDTO);
        });
        return result;
    }

    private ProfileEntity toEntity(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setName(profileDTO.getName());
        profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
        profileEntity.setEmail(profileDTO.getEmail());
        profileEntity.setRole(ProfileRoleEnum.USER);
        profileEntity.setStatus(ProfileStatusEnum.ACTIVE);
        profileEntity.setCreatedAt(LocalDateTime.now());
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setPhoneNumber(profileDTO.getPhoneNumber());

        return profileEntity;
    }

    public String deleteById(int id){
        Optional<ProfileEntity> byId = profileRepository.findById(id);

        if(byId.isEmpty()){
            throw new ItemNotFoundException("Profile with id " + id + " not found");
        }
        profileRepository.deleteById(id);
        return "Profile deleted";
    }

    private ProfileEntity getById(int id){
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        return byId.orElse(null);
    }

    public String update(ProfileDTO profileDTO, int id) {
        ProfileEntity byId = getById(id);

        if(byId == null)
            throw new ItemNotFoundException("User with given id is not found");

        byId.setSurname(profileDTO.getSurname());
        byId.setName(profileDTO.getName());
        byId.setPassword(profileDTO.getPassword());
        byId.setEmail(profileDTO.getEmail());

        profileRepository.save(byId);
        return "Profile Updated";
    }
}

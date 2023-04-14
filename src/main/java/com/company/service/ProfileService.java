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
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // TODO: 4/14/2023 Make this method to be accessible only by admins
    public ProfileDTO create(ProfileDTO profileDTO, JwtDTO jwtDTO) {

        //Validation
        if (profileDTO.getName().isEmpty() || profileDTO.getName().isBlank())
            throw new ProfileCreateException("Name field is empty");
        if (profileDTO.getSurname().isBlank() || profileDTO.getSurname().isEmpty())
            throw new ProfileCreateException("Surname field is empty");
        if (!profileDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"))
            throw new ProfileCreateException("Password should have at least one number, one lowercase or uppercase character" +
                    " and the length should be at least 8 letters");
        if (!profileDTO.getPhoneNumber().matches("[+]998[0-9]{9}"))
            throw new ProfileCreateException("Phone number should be in the following format: +998 xx xxx-xx-xx");

        if (!jwtDTO.getRole().equals(ProfileRoleEnum.ADMIN))
            throw new AppForbiddenException("Method not allowed");

        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(profileDTO.getEmail());
        if (byEmail.isPresent())
            throw new ProfileCreateException("User with this email already exists");
        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneNumber(profileDTO.getPhoneNumber());
        if (byPhoneNumber.isPresent())
            throw new ProfileCreateException("User with this phone number already exists");

        profileRepository.save(toEntity(profileDTO));
        return profileDTO;
    }

    public Page<ProfileDTO> getProfileList(int page, int size, JwtDTO jwtDTO) {
        if (!jwtDTO.getRole().equals(ProfileRoleEnum.ADMIN))
            throw new AppForbiddenException("Method Not Allowed");
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
        profileEntity.setRole(profileDTO.getRole());
        profileEntity.setStatus(ProfileStatusEnum.ACTIVE);
        profileEntity.setCreatedAt(LocalDateTime.now());
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setPhoneNumber(profileDTO.getPhoneNumber());
        profileEntity.setVisible(true);
        return profileEntity;
    }

    public String deleteById(int id, JwtDTO jwtDTO) {
        if (!jwtDTO.getRole().equals(ProfileRoleEnum.ADMIN))
            throw new AppForbiddenException("Method not Allowed");
        Optional<ProfileEntity> byId = profileRepository.findById(id);

        if (byId.isEmpty()) {
            throw new ItemNotFoundException("Profile with id " + id + " not found");
        }
        profileRepository.deleteById(id);
        return "Profile deleted";
    }

    private ProfileEntity getById(int id) {
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        return byId.orElse(null);
    }

    public String update(ProfileDTO profileDTO, int id, JwtDTO jwtDTO) {
        if (!jwtDTO.getRole().equals(ProfileRoleEnum.ADMIN))
            throw new AppForbiddenException("Method not Allowed");

        int b = profileRepository.updateByAdminByProfileId(profileDTO.getName(), profileDTO.getSurname(), profileDTO.getRole(), profileDTO.getStatus(), id);

        if (b <= 0)
            throw new RuntimeException("Not updated");
        return "Profile Updated";
    }

    public String updateByProfile(ProfileDTO profileDTO, JwtDTO jwtDTO) {
        int b = profileRepository.updateByAll(profileDTO.getName(), profileDTO.getSurname(), jwtDTO.getId());

        if(b <=0)
            throw new RuntimeException("User not updated");

        return "Profile Updated";
    }
}

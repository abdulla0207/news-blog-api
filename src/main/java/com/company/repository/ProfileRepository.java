package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity> findByEmailAndPhoneNumberAndPassword(String email, String phoneNumber, String password);

    Optional<ProfileEntity> findByPhoneNumber(String phoneNum);

    Optional<ProfileEntity> findProfileEntitiesByRole(ProfileRoleEnum profileRoleEnum);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set name =?1, surname = ?2, role = ?3, status = ?4 where id = ?5")
    int updateByAdminByProfileId(String name, String surname, ProfileRoleEnum profileRoleEnum , ProfileStatusEnum statusEnum, int id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set name = ?1, surname = ?2 where id = ?3")
    int updateByAll(String name, String surname, int id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.status=com.company.enums.ProfileStatusEnum.ACTIVE where p.email=?1")
    void activateProfileStatus(String email);
}

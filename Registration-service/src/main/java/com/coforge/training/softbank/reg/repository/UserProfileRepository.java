package com.coforge.training.softbank.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.softbank.reg.model.UserProfile;

/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 9:31:23 pm
* Project:registration-service
**/

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}

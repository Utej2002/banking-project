package com.coforge.training.softbank.reg.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 8:33:26 pm
* Project:registration-service
**/

@Entity
@Table(name = "account_requests")
@Setter
@Getter
public class AccountRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String accountNo;
 
    @Column(nullable = false)
    private String accountType;
 
    @Column(nullable = false)
    private String status = "Pending"; // 'Pending', 'Approved', 'Rejected'
 
    private LocalDateTime createdAt = LocalDateTime.now();
 
    private LocalDateTime approvedAt;
    
    @Lob
    @Column(name = "aadhar_pdf", columnDefinition = "LONGBLOB")
    private byte[] aadharPdf;

    @Lob
    @Column(name = "passport_photo", columnDefinition = "LONGBLOB")
    private byte[] passportPhoto;
 
    // Relationship with UserProfile
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Setter
    @Getter
    private UserProfile userProfile;
 
    // Getters and Setters
}

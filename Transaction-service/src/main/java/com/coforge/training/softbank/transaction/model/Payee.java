package com.coforge.training.softbank.transaction.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:47:11 am
* Project:Transaction-service
**/

@Entity
@Table(name = "payees")
@Getter
@Setter
public class Payee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;
    private String name;
    private String upiId;
}

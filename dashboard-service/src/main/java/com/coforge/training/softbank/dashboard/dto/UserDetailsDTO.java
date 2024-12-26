package com.coforge.training.softbank.dashboard.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 17-Dec-2024
* Time   : 10:46:55 am
* Project:dashboard-service
**/

@Getter
@Setter
public class UserDetailsDTO {
    private String username;
    private String accountNo;
    private String upiId;
    private BigDecimal balance;
}
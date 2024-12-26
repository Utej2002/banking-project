package com.coforge.training.softbank.transaction.dto;

import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:50:47 am
* Project:Transaction-service
**/

@Getter
@Setter
public class PayeeDTO {
    private Long id;
    private String accountNo;
    private String name;
    private String upiId;
}
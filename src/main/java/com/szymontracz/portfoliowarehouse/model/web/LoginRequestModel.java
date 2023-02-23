package com.szymontracz.portfoliowarehouse.model.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestModel {
    private String email;
    private String password;

}

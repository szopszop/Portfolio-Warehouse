/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.szymontracz.model.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateUserRequestModel {
    
    @NotNull(message="First name cannot be null")
    @Size(min=2, message = "First name must not be less than 2 characters")
    private String firstName;
    
	
    @NotNull(message="Last name cannot be null")
    @Size(min=2, message = "Last name must not be less than 2 characters")
    private String lastName;
    
    @NotNull(message="Password cannot be null")
    @Size(min=8,max=16, message="Password must be equal or grater than 8 characters and less than 16 chaeracters")
    private String password;
    
    @NotNull(message="Email cannot be null")
    @Email
    private String email;


}

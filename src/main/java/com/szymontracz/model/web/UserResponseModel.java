/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.szymontracz.model.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseModel {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AlbumResponseModel> albums;


}

package com.szymontracz.warehouse.model.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseModel {
    private String userId;
    private String username;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AlbumResponseModel> albums;
}

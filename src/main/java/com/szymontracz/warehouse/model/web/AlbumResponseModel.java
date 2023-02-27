package com.szymontracz.warehouse.model.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumResponseModel {
    private String albumId;
    private String userId;
    private String title;
}

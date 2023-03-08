package com.szymontracz.warehouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private Long id;
    private String title;
    private String path;
    private LocalDateTime createdDate;


}

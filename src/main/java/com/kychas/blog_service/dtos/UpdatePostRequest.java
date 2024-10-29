package com.kychas.blog_service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequest {
    private String title;
    private String content;
}

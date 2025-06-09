package com.neolearn.projects_service.application.dto.resource;
import lombok.Data;
@Data public class CreateResourceRequest {
    private String name; private String type; private String location;
}
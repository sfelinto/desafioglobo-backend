package br.com.desafioglobo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafioglobo.resource.ApiLinksResource;

@RestController
public class ApiController {

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiLinksResource> getControllerLinks() {
        return ResponseEntity.ok(new ApiLinksResource());
    }

}

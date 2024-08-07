package com._119.wepro.project.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  @GetMapping()
  public ResponseEntity<String> getProjects() {
    return ResponseEntity.ok("Hello World");
  }
}

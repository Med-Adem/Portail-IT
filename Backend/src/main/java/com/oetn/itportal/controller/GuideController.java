package com.oetn.itportal.controller;

import com.oetn.itportal.dto.GuideDto;
import com.oetn.itportal.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/guides")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class GuideController {

    @Autowired
    private GuideService guideService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GuideDto> upload(@RequestParam("title") String title,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(guideService.uploadGuide(title, file));
    }

    @GetMapping
    public ResponseEntity<List<GuideDto>> getAll() {
        return ResponseEntity.ok(guideService.getAllGuides());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource resource = guideService.downloadGuide(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IOException {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }
}

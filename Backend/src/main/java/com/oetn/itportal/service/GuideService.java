package com.oetn.itportal.service;

import com.oetn.itportal.dto.GuideDto;
import com.oetn.itportal.model.Guide;
import com.oetn.itportal.repository.GuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public GuideDto uploadGuide(String title, MultipartFile file) throws IOException {
        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Seuls les fichiers PDF sont acceptés.");
        }
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Guide guide = Guide.builder()
                .title(title)
                .fileName(file.getOriginalFilename())
                .filePath(filePath.toString())
                .fileSize(file.getSize())
                .build();
        return mapToDto(guideRepository.save(guide));
    }

    public List<GuideDto> getAllGuides() {
        return guideRepository.findAllByOrderByUploadedAtDesc()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Resource downloadGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide introuvable: " + id));
        try {
            Path filePath = Paths.get(guide.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) return resource;
            throw new RuntimeException("Fichier introuvable: " + guide.getFileName());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur chargement fichier: " + e.getMessage());
        }
    }

    public void deleteGuide(Long id) throws IOException {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide introuvable: " + id));
        Files.deleteIfExists(Paths.get(guide.getFilePath()));
        guideRepository.deleteById(id);
    }

    private GuideDto mapToDto(Guide guide) {
        return GuideDto.builder()
                .id(guide.getId())
                .title(guide.getTitle())
                .fileName(guide.getFileName())
                .fileSize(guide.getFileSize())
                .uploadedAt(guide.getUploadedAt())
                .downloadUrl("/api/guides/" + guide.getId() + "/download")
                .build();
    }
}

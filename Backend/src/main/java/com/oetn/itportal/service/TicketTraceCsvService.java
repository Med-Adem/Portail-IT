package com.oetn.itportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TicketTraceCsvService {

    @Value("${app.ticket-traces.dir:traces_tickets}")
    private String baseDir;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public synchronized void trace(
            String ticketType,
            Long ticketId,
            String action,
            String oldStatus,
            String newStatus,
            String username,
            LocalDateTime createdAt,
            LocalDateTime processedAt,
            String details
    ) {
        try {
            LocalDateTime effectiveProcessedAt = processedAt != null ? processedAt : LocalDateTime.now();
            Path filePath = buildFilePath(ticketType, effectiveProcessedAt);

            Files.createDirectories(filePath.getParent());

            if (Files.notExists(filePath) || Files.size(filePath) == 0) {
                writeHeader(filePath);
            }

            String line = String.join(";",
                    csv(ticketId != null ? ticketId.toString() : ""),
                    csv(ticketType),
                    csv(action),
                    csv(createdAt != null ? createdAt.format(DATE_TIME_FORMAT) : ""),
                    csv(effectiveProcessedAt.format(DATE_TIME_FORMAT)),
                    csv(oldStatus != null ? oldStatus : ""),
                    csv(newStatus != null ? newStatus : ""),
                    csv(username != null ? username : ""),
                    csv(details != null ? details : "")
            ) + System.lineSeparator();

            Files.write(
                    filePath,
                    line.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture des traces tickets.", e);
        }
    }

    private Path buildFilePath(String ticketType, LocalDateTime dateTime) {
        String year = String.valueOf(dateTime.getYear());
        String month = String.format("%02d", dateTime.getMonthValue());
        String day = String.format("%02d", dateTime.getDayOfMonth());

        String normalizedType = normalize(ticketType);
        String fileName = "traces_" + normalizedType + "_" + dateTime.toLocalDate().format(DATE_FORMAT) + ".csv";

        return Paths.get(baseDir, year, month, day, normalizedType, fileName);
    }

    private void writeHeader(Path filePath) throws IOException {
        String header = "ticket_id;ticket_type;action;date_creation;date_traitement;statut_initial;statut_final;utilisateur;details"
                + System.lineSeparator();

        Files.write(
                filePath,
                header.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    private String normalize(String value) {
        return value.toLowerCase().trim().replace(" ", "_");
    }

    private String csv(String value) {
        String safe = value.replace("\"", "\"\"")
                .replace("\r", " ")
                .replace("\n", " ");
        return "\"" + safe + "\"";
    }
}
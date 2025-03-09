package com.aqua.flux.service;

import com.aqua.flux.model.Artifact;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ArtifactService {

    private static final String JSON_FILE_PATH = "src/main/resources/artifacts.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Read all artifacts
    public List<Artifact> getAllArtifacts() {
        try {
            return objectMapper.readValue(new File(JSON_FILE_PATH), new TypeReference<List<Artifact>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Save artifacts to JSON file
    private void saveArtifacts(List<Artifact> artifacts) {
        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), artifacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add new artifact
    public Artifact addArtifact(Artifact artifact) {
        List<Artifact> artifacts = getAllArtifacts();
        artifact.setId((long) (artifacts.size() + 1)); // Generate new ID
        artifacts.add(artifact);
        saveArtifacts(artifacts);
        return artifact;
    }

    // Delete an artifact by ID
    public boolean deleteArtifact(Long id) {
        List<Artifact> artifacts = getAllArtifacts();
        boolean removed = artifacts.removeIf(a -> a.getId().equals(id));
        if (removed) {
            saveArtifacts(artifacts);
        }
        return removed;
    }

    // Update an artifact by ID
    public Artifact updateArtifact(Long id, Artifact updatedArtifact) {
        List<Artifact> artifacts = getAllArtifacts();
        Optional<Artifact> existingArtifact = artifacts.stream().filter(a -> a.getId().equals(id)).findFirst();
        if (existingArtifact.isPresent()) {
            Artifact artifact = existingArtifact.get();
            artifact.setName(updatedArtifact.getName());
            artifact.setCategory(updatedArtifact.getCategory());
            artifact.setPrice(updatedArtifact.getPrice());
            artifact.setStock(updatedArtifact.getStock());
            saveArtifacts(artifacts);
            return artifact;
        }
        return null;
    }

    // Find artifacts by category
    public List<Artifact> findByCategory(String category) {
        return getAllArtifacts().stream()
                .filter(artifact -> artifact.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}

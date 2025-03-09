package com.aqua.flux.artifact.controller;

import com.aqua.flux.model.Artifact;
import com.aqua.flux.service.ArtifactService;
import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ArtifactGraphQLController {

  private final ArtifactService artifactService;

  public ArtifactGraphQLController(ArtifactService artifactService) {
    this.artifactService = artifactService;
  }

  // Query: Get all artifacts
  @QueryMapping
  public List<Artifact> getAllArtifacts() {
    return artifactService.getAllArtifacts();
  }

  // Query: Find artifacts by category
  @QueryMapping
  public List<Artifact> findByCategory(@Argument String category) {
    return artifactService.findByCategory(category);
  }

  // Mutation: Add a new artifact
  @MutationMapping
  public Artifact addArtifact(
      @Argument String name,
      @Argument String category,
      @Argument Double price,
      @Argument Integer stock) {
    Artifact artifact = new Artifact(null, name, category, price, stock);
    return artifactService.addArtifact(artifact);
  }

  // Mutation: Update an existing artifact
  @MutationMapping
  public Artifact updateArtifact(
      @Argument Long id,
      @Argument String name,
      @Argument String category,
      @Argument Double price,
      @Argument Integer stock) {

    Artifact updatedArtifact = new Artifact(id, name, category, price, stock);
    return artifactService.updateArtifact(id, updatedArtifact);
  }

  // Mutation: Delete an artifact by ID
  @MutationMapping
  public Boolean deleteArtifact(@Argument Long id) {
    return artifactService.deleteArtifact(id);
  }
}

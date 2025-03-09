package com.aqua.flux.service;

import com.aqua.flux.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
  private static final String JSON_FILE_PATH = "src/main/resources/products.json";
  private final ObjectMapper objectMapper;

  public ProductService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<Product> getProductsFromJson(
      Integer page,
      Integer size,
      String sortBy,
      String order,
      String search,
      Double minPrice,
      Double maxPrice,
      String category) {
    try {
      List<Product> products =
          objectMapper.readValue(new File(JSON_FILE_PATH), new TypeReference<List<Product>>() {});

      // Filtering by search query
      if (search != null && !search.isEmpty()) {
        products =
            products.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
      }

      // Filtering by category
      if (category != null && !category.isEmpty()) {
        products =
            products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
      }

      // Filtering by price range
      if (minPrice != null) {
        products =
            products.stream().filter(p -> p.getPrice() >= minPrice).collect(Collectors.toList());
      }
      if (maxPrice != null) {
        products =
            products.stream().filter(p -> p.getPrice() <= maxPrice).collect(Collectors.toList());
      }

      // Sorting
      if (sortBy != null) {
        Comparator<Product> comparator =
            switch (sortBy) {
              case "name" -> Comparator.comparing(Product::getName);
              case "price" -> Comparator.comparing(Product::getPrice);
              case "category" -> Comparator.comparing(Product::getCategory);
              default -> Comparator.comparing(Product::getId);
            };
        if ("desc".equalsIgnoreCase(order)) {
          comparator = comparator.reversed();
        }
        products = products.stream().sorted(comparator).collect(Collectors.toList());
      }

      // Pagination
      if (page != null && size != null) {
        int start = page * size;
        int end = Math.min(start + size, products.size());
        if (start < products.size()) {
          products = products.subList(start, end);
        } else {
          products = List.of();
        }
      }

      return products;
    } catch (IOException e) {
      e.printStackTrace();
      return List.of();
    }
  }

  public Boolean importProducts(List<Product> products) {
    try {
      List<Product> existingProducts =
          objectMapper.readValue(new File(JSON_FILE_PATH), new TypeReference<List<Product>>() {});
      existingProducts.addAll(products);
      objectMapper.writeValue(new File(JSON_FILE_PATH), existingProducts);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public String exportProducts() {
    try {
      return Files.readString(Paths.get(JSON_FILE_PATH));
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
}

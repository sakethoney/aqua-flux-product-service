package com.aqua.flux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artifact {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
}

package com.aqua.flux.product.controller;

import com.aqua.flux.product.model.Product;
import com.aqua.flux.product.model.InputProduct;
import com.aqua.flux.product.service.ProductService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;


@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> getAllProducts(@Argument Integer page, @Argument Integer size, @Argument String sortBy, @Argument String order, @Argument String search, @Argument Double minPrice, @Argument Double maxPrice, @Argument String category) {
        return productService.getProductsFromJson(page, size, sortBy, order, search, minPrice, maxPrice, category);
    }

    @MutationMapping
    public Boolean importProducts(@Argument List<InputProduct> products) {
        List<Product> productList = products.stream().map(p -> new Product( null, p.getName(), p.getPrice(), p.getCategory())).toList();
        return productService.importProducts(productList);
    }

    @QueryMapping
    public String exportProducts() {
        return productService.exportProducts();
    }
}

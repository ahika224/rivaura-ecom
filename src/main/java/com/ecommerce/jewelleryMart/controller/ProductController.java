package com.ecommerce.jewelleryMart.controller;

import com.ecommerce.jewelleryMart.model.Product;
import com.ecommerce.jewelleryMart.repository.ProductRepository;
import com.ecommerce.jewelleryMart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.ecommerce.jewelleryMart.constant.Constant.PHOTO_DIRECTORY_PRODUCT;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String metalType
    ) {
        List<Product> products = productService.getProductList(search, sort, category, metalType);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getByProductId(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/metalTypes/{category}")
    public ResponseEntity<List<String>> getAllMetalTypes(@PathVariable String category) {
        List<String> metalTypes = productService.getAllMetalTypes( category);
        if(metalTypes.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonList("No Metal Types"));
        }
        else {
            return ResponseEntity.ok(metalTypes);
        }
    }
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> allCategories = productService.getAllCategories( );
        if(allCategories.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonList("No Categories found"));
        }
        else {
            return ResponseEntity.ok(allCategories);
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {


            Product savedProduct = productService.updateProduct(id,productDetails);
if(savedProduct!=null){


            return ResponseEntity.ok(savedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        boolean deleted=productService.deleteProduct(id);
        if (deleted) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/photos")
    public ResponseEntity<List<String>> uploadPhoto(@RequestParam("productID") Long productID,  @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok().body(productService.uploadPhoto(productID, files));
    }



    @GetMapping(path = "/image/{productID}/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("productID") Long productID,@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY_PRODUCT + productID+"/"+filename));
    }

}

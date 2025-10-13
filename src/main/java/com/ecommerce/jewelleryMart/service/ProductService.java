package com.ecommerce.jewelleryMart.service;

import com.ecommerce.jewelleryMart.model.Product;
import com.ecommerce.jewelleryMart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ecommerce.jewelleryMart.constant.Constant.PHOTO_DIRECTORY_PRODUCT;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public List<Product> getProductList(String search, String sort, String category, String metalType) {
        List<Product> products = (search != null && !search.isEmpty())
                ? productRepository.findByNameContainingIgnoreCase(search)
                : productRepository.findAll();

        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        if (metalType != null && !metalType.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getMetalType() != null && p.getMetalType().equalsIgnoreCase(metalType))
                    .collect(Collectors.toList());
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "priceLowToHigh":
                    products.sort(Comparator.comparingDouble(Product::getPrice));
                    break;
                case "priceHighToLow":
                    products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                    break;
                case "nameAsc":
                    products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "nameDesc":
                    products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                default:
                    break;
            }
        }
        return products;
    }
    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public List<String> uploadPhoto(Long id, List<MultipartFile> files) {
        log.info("Saving picture for product ID: {}", id);
        Product product = getProduct(id);
        List<String> imageStrings=files.stream().map(file->{
            return photoFunction.apply(String.valueOf(id), file);
        }).collect(Collectors.toList());
        for(MultipartFile file:files) {

        }
        product.setImages(imageStrings);
        productRepository.save(product);
        return imageStrings;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {

           String filename = image.getOriginalFilename();
           try {
               Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY_PRODUCT+id+"/").toAbsolutePath().normalize();
               if (!Files.exists(fileStorageLocation)) {
                   Files.createDirectories(fileStorageLocation);
               }
               Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
               return ServletUriComponentsBuilder
                       .fromCurrentContextPath()
                       .path("/product/image/" +id+"/"+ filename).toUriString();
           } catch (Exception exception) {
               throw new RuntimeException("Unable to save image");
           }

    };

    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        Product productUp=null;




        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Update ALL fields explicitly
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setCategory(productDetails.getCategory());
            product.setMetalType(productDetails.getMetalType());
            product.setImages(productDetails.getImages());
            product.setDescription(productDetails.getDescription());
            product.setWeight(productDetails.getWeight());
            product.setQuantity(productDetails.getQuantity());// EXPLICITLY SET WEIGHT
productUp=productRepository.save(product);
            return productUp;
        }
        else{
            return null;
        }


    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        else{
            return false;
        }
    }
 public List<String> getAllMetalTypes(String category){
        List<String> metalTypes=productRepository.getAllMetalTypes(category);
        return metalTypes;
 }
    public Optional<Product> getByProductId(Long id) {
        return productRepository.findById(id);
    }

    public List<String> getAllCategories() {
        return productRepository.getAllCategories();
    }
}

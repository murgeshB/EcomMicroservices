package com.ecommerce.product.services;


import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public void createProduct(ProductDTO product){
        productRepository.save(modelMapper.map(product, Product.class));
    }

    public List<ProductDTO> getAllProducts(){
        return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(Long id){
        return productRepository.findById(id).map(product -> modelMapper.map(product,ProductDTO.class));
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}

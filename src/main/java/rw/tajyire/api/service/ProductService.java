package rw.tajyire.api.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rw.tajyire.api.dto.product.ProductCreationDTO;
import rw.tajyire.api.dto.product.ProductModificationDTO;
import rw.tajyire.api.exception.EntityNotFoundException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Product;
import rw.tajyire.api.model.SubCategory;
import rw.tajyire.api.repo.ProductRepo;
import rw.tajyire.api.util.AES;

@Service
public class ProductService {
  @Autowired private CloudinaryService cloudinaryService;
  @Autowired private ProductRepo productRepo;

  public Product findByUuId(String productUuId) {
    return productRepo
        .findByUuidAndDeletedIsFalse(productUuId)
        .orElseThrow(() -> new EntityNotFoundException("Could not find product"));
  }

  public Page<Product> getProducts(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "creationDate"));
    return productRepo.getAllByDeletedIsFalse(pageable);
  }

  public Product createProduct(ProductCreationDTO dto, Admin admin) throws ParseException {
    List<String> images = new ArrayList<>();
    Arrays.asList(dto.getImages()).stream()
        .forEach(
            image -> {
              images.add(cloudinaryService.uploadFile(image));
            });
    String imageUrlsStr = JSONArray.toJSONString(images);

    Product product = new Product();
    product.setName(dto.getName());
    product.setPrice(dto.getPrice());
    product.setPriceCurrency(dto.getPriceCurrency());
    product.setCategory(new SubCategory(dto.getCategoryId()));
    product.setImageUrls(imageUrlsStr);
    product.setCreatedBy(admin.getUuid());
    if (dto.getDiscountPrice() != null) {
      product.setDiscountExpiryDate(
          new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDiscountExpiryDate()));
      product.setDiscountPrice(dto.getDiscountPrice());
    }
    return productRepo.save(product);
  }

  public Product editProduct(String productUuId, ProductModificationDTO dto, Admin admin)
      throws org.json.simple.parser.ParseException, ParseException {

    Product product = findByUuId(productUuId);
    List<String> images = new ArrayList<>();
    Arrays.asList(dto.getNewImages()).stream()
        .forEach(
            image -> {
              images.add(cloudinaryService.uploadFile(image));
            });
    JSONParser parser = new JSONParser();
    JSONArray array = (JSONArray) parser.parse(product.getImageUrls());
    if (array != null) {
      images.addAll(array);
    }
    for (String removedImage : dto.getRemovedImage()) {
      images.remove(removedImage);
    }
    String imageUrlsStr = JSONArray.toJSONString(images);
    product.setName(dto.getName());
    product.setPrice(dto.getPrice());
    product.setPriceCurrency(dto.getPriceCurrency());
    product.setCategory(new SubCategory(dto.getCategoryId()));
    product.setImageUrls(imageUrlsStr);
    product.setCreatedBy(admin.getUuid());
    if (dto.getDiscountPrice() != null) {
      product.setDiscountExpiryDate(
          new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDiscountExpiryDate()));
      product.setDiscountPrice(dto.getDiscountPrice());
    }
    return productRepo.save(product);
  }

  public Product removeProduct(String productUuId, Admin admin) {
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);
    Product foundProduct = findByUuId(productUuId);
    foundProduct.setDeleted(true);
    foundProduct.setLastDeletedBy(admin.getUuid());
    foundProduct.setLastDeletionDate(date);
    foundProduct.setName(AES.encrypt(foundProduct.getName() + " <-DELETED ON:-> " + strDate));
    return productRepo.save(foundProduct);
  }
}

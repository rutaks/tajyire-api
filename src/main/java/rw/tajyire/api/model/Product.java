package rw.tajyire.api.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.tajyire.api.dto.product.ProductCreationDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;
  private String uuid = UUID.randomUUID().toString();
  private String name;
  private String imageUrls;
  private Double price;
  private String priceCurrency;
  private Double discountPrice;
  @Temporal(TemporalType.DATE)
  private Date discountExpiryDate;
  private String createdBy;
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;
  private String lastModifiedBy;
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModificationDate;
  private boolean deleted = false;
  private String lastDeletedBy;
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastDeletionDate;
  @ManyToOne
  @JoinColumn(name = "product_category", nullable = false)
  private SubCategory category;
}

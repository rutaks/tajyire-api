package rw.tajyire.api.model;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rw.tajyire.api.dto.category.SubCategoryDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "sub_category",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "parent_category"})})
public class SubCategory {

  @Temporal(TemporalType.TIMESTAMP)
  protected Date creationDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastModifiedDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastDeletedDate;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "parent_category", nullable = false)
  private Category parentCategory;

  private String uuid = UUID.randomUUID().toString();

  @Column(name = "name")
  private String name;

  private String imageCover;
  private boolean deleted = false;
  private String createdBy;
  private String lastModifiedBy;
  private String lastDeletedBy;

  @OneToMany(fetch = FetchType.LAZY)
  private Set<Product> products;

  public SubCategory(Long id) {
    this.id = id;
  }

  public SubCategory(SubCategoryDTO subCategoryDTO) {
    this.name = subCategoryDTO.getName();
    this.parentCategory = new Category(subCategoryDTO.getParentCategoryId());
  }
}

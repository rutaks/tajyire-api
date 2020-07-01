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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rw.tajyire.api.dto.category.CategoryDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table
public class Category {
  @Temporal(TemporalType.TIMESTAMP)
  protected Date creationDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastModifiedDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastDeletedDate;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  @OneToMany(fetch = FetchType.LAZY)
  private Set<SubCategory> categories;
  private String uuid = UUID.randomUUID().toString();
  @Column(unique = true)
  private String name;
  private String imageCover;
  private String createdBy;
  private String lastModifiedBy;
  private String lastDeletedBy;
  private boolean deleted = false;

  public Category(Long id) {
    this.id = id;
  }
  public Category(CategoryDTO categoryDTO) {
    this.name = categoryDTO.getName();
  }
}

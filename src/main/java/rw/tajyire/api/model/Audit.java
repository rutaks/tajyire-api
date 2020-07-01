package rw.tajyire.api.model;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Audit {
  @Temporal(TemporalType.TIMESTAMP)
  protected Date creationDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastModifiedDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastDeletedDate;
  private String createdBy;
  private String lastModifiedBy;
  private String lastDeletedBy;
}

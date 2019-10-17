package com.imooc.product.dataobject;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ProductCategory {
    @Id
    @Generated
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;
    private Date CreateTime;
    private Date UpdateTime;
}

package org.sang.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseEntity<ID extends Serializable> implements Serializable{

    private static final long serialVersionUID = -8724989888918147700L;

    private ID id;
    private Date createTime = new Date();
    private Date updateTime = new Date();

}

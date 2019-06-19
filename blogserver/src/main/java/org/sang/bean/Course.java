package org.sang.bean;

import lombok.Data;



@Data
public class Course extends BaseEntity<Long> {

    private static final long serialVersionUID = 1962032166751370211L;

    private String courseId;
    private String courseName;
    private Integer coursePeriod;
    private String courseDescription;
    private String teacherId;

}

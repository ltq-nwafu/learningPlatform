package org.sang.bean;


import lombok.Data;

/**
 * @Author: ss
 * @Description: 课程章节信息记录表
 * @Date: Created in 2019/5/25
 */

@Data
public class CourseChapterSection extends BaseEntity<Long> {

    private static final long serialVersionUID = 5449524574331330141L;

    private String chapterSectionId;
    private String chapterSerialNumber;
    private String chapterName;
    private String sectionQuantity;
    private String courseId;
    private String teacherId;


}

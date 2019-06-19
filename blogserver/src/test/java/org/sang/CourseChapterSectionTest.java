package org.sang;


import org.junit.Test;
import org.sang.bean.CourseChapterSection;
import org.sang.controller.CourseChapterSectionController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class CourseChapterSectionTest extends BlogServerApplicationTest {

    @Autowired
    private CourseChapterSectionController courseChapterSectionController;

    @Test
    public void insert() {
        CourseChapterSection ccs = new CourseChapterSection();
        ccs.setChapterSerialNumber("第一章");
        ccs.setChapterName("引言");
        ccs.setSectionQuantity("1");
        ccs.setCourseId(String.valueOf(System.currentTimeMillis()));
        ccs.setTeacherId("3");
        courseChapterSectionController.add(ccs);
        System.out.println("插入成功！");
    }

    @Test
    public void select() {
        Map<String,Object> map = courseChapterSectionController.getCourseChapterSectionListByTeacherIdCourseId(1,1,"3","1559046113862","引言");
        System.out.println("共几条数据？！");
        System.out.println("totalCount:" + map.get("totalCount") + " ccs:" + map.get("courseChapterSections").toString());
        System.out.println("查询成功！");
    }
}

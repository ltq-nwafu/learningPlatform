package org.sang;

import org.junit.Test;
import org.sang.bean.Course;
import org.sang.controller.CourseController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class CourseControllerTest extends BlogServerApplicationTest {

    @Autowired
    private CourseController courseController;



    @Test
    public void insert() {
        Course course = new Course();
        course.setCourseName("vue");
        course.setCoursePeriod(20);
        course.setCourseDescription("前端框架");
//        course.setTeacherId("3");
        courseController.add(course);
        System.out.println("插入成功！");
    }

    @Test
    public void getCourseListByTeacherId() {
        Map<String,Object> map = courseController.getCourseListByTeacherId(1, 1, "a");
        System.out.println("共几条数据？！");
        System.out.println("totalCount:" + map.get("totalCount") + " courses:" + map.get("courses").toString());
        System.out.println("查询成功！");
    }


    @Test
    public void delete() {
        courseController.deleteCourseByIds("18");
    }

}

package org.sang.service;

import org.sang.bean.Course;
import org.sang.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseMapper courseMapper;
    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }



    //分页
    //显示课程
    public List<Course> getCourseListByTeacherId(Integer pagination, Integer rows, String teacherId) {
        int offset = (pagination - 1) * rows;
        return courseMapper.getCourseListByTeacherId(offset, rows, teacherId);
    }

    public int getCourseQuantityByTeacherId(String teacherId) {
        return courseMapper.getCourseQuantityByTeacherId(teacherId);
    }

    //分页
    //搜索课程
    public List<Course> getCourseListBySearch(Integer pagination, Integer rows, String teacherId, String idOrName) {
        int offset = (pagination - 1) * rows;
        return courseMapper.getCourseListBySearch(offset, rows, teacherId, idOrName);
    }

    public int getCourseQuantityBySearch(String teacherId, String idOrName) {
        return courseMapper.getCourseQuantityBySearch(teacherId, idOrName);
    }


    //添加课程
    public int addCourse(Course course) {
        course.setCourseId(String.valueOf(System.currentTimeMillis()));
        course.setTeacherId("3");
        return courseMapper.addCourse(course);
    }

    //删除课程
    public boolean deleteCourseByIds(String ids) {
        String[] split = ids.split(",");
        int result = courseMapper.deleteCourseByIds(split);
        return result == split.length;
    }

    //编辑课程
    public int updateCourseById(Course course) {
        return courseMapper.updateCourseById(course);
    }


}

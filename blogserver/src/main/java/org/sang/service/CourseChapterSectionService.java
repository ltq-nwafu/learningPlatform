package org.sang.service;

import org.sang.bean.CourseChapterSection;
import org.sang.mapper.CourseChapterSectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CourseChapterSectionService {
    private final CourseChapterSectionMapper courseChapterSectionMapper;
    @Autowired
    public CourseChapterSectionService(CourseChapterSectionMapper courseChapterSectionMapper) {
        this.courseChapterSectionMapper = courseChapterSectionMapper;
    }

    //添加章节
    public int addCourseChapterSection(CourseChapterSection courseChapterSection) {
        courseChapterSection.setChapterSectionId(String.valueOf(new Random().nextLong()));
        return courseChapterSectionMapper.addChapterSection(courseChapterSection);
    }

    //分页
    //显示章节
    public List<CourseChapterSection> getCourseChapterSectionListByTeacherIdCourseId(Integer pagination, Integer rows, String teacherId, String courseId) {
        int offset = (pagination - 1) * rows;
        return courseChapterSectionMapper.getCourseChapterSectionListByTeacherIdCourseId(offset, rows,teacherId, courseId);
    }

    public int getCourseChapterSectionQuantityByTeacherIdCourseId(String teacherId, String courseId) {
        return courseChapterSectionMapper.getCourseChapterSectionQuantityByTeacherIdCourseId(teacherId, courseId);
    }

    //分页
    //搜索章节
    public List<CourseChapterSection> getCourseChapterSectionListBySearch(Integer pagination, Integer rows, String teacherId, String courseId, String idOrName) {
        int offset = (pagination - 1) * rows;
        return courseChapterSectionMapper.getCourseChapterSectionListBySearch(offset, rows, teacherId, courseId, idOrName);
    }

    public int getCourseChapterSectionQuantityBySearch(String teacherId, String courseId, String idOrName) {
        return courseChapterSectionMapper.getCourseChapterSectionQuantityBySearch(teacherId, courseId, idOrName);
    }


    //编辑章节
    public int updateCourseChapterSectionById(CourseChapterSection courseChapterSection) {
        return courseChapterSectionMapper.updateCourseChapterSectionById(courseChapterSection);
    }


    //删除章节
    public boolean deleteCourseChapterSectionByIds(String ids) {
        String[] split = ids.split(",");
        int result = courseChapterSectionMapper.deleteCourseChapterSectionByIds(split);
        return result == split.length;
    }


}

package org.sang.mapper;

import org.apache.ibatis.annotations.*;
import org.sang.bean.CourseChapterSection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CourseChapterSectionMapper {

    //在表course_chapter_section中插入章信息
//    @Options(useGeneratedKeys = true)
//    @Insert("insert into course_chapter_section(chapterSectionId, chapterSerialNumber, chapterName, sectionQuantity, courseId, teacherId) values(#{chapterSectionId}, #{chapterSerialNumber}, #{chapterName}, #{sectionQuantity},#{courseId}, #{teacherId})")
    int addChapterSection(CourseChapterSection courseChapterSection);

    //分页
    //显示章节
    //根据teacherId和courseId从表course_chapter_section中查询该教师该课程章节列表，从偏移量offset开始，初始行的偏移量为0，查询记录行数rows，用于分页展示
//    @Select("select * from course_chapter_section where teacherId=#{teacherId} and courseId=#{courseId} limit #{offset},#{rows}")
    List<CourseChapterSection> getCourseChapterSectionListByTeacherIdCourseId(@Param("offset") Integer offset, @Param("rows") Integer rows, @Param("teacherId") String teacherId, @Param("courseId") String courseId);

    //根据teacherId和courseId从表course_chapter_section中查询该教师该课程的章数量
//    @Select("select count(*) from course_chapter_section where teacherId=#{teacherId} and courseId=#{courseId}")
    int getCourseChapterSectionQuantityByTeacherIdCourseId(@Param("teacherId") String teacherId, @Param("courseId") String courseId);

    //分页
    //搜索课程
    //根据teacherId和courseId和chapterName从表course_chapter_section中查询该教师该课程该章或名称相似章记录列表
//    @Select("select * from course_chapter_section where teacherId=#{teacherId} AND courseId=#{courseId} AND chapterName like concat('%', #{idOrName}, '%') limit #{offset},#{rows}")
    List<CourseChapterSection> getCourseChapterSectionListBySearch(@Param("offset") Integer offset, @Param("rows") Integer rows, @Param("teacherId") String teacherId, @Param("courseId") String courseId, @Param("idOrName") String idOrName);

    //根据teacherId和courseId和chapterName从表course中查询该教师该课程该章或名称相似章记录数量
//    @Select("select count(*) from course_chapter_section where teacherId=#{teacherId} AND courseId =#{courseId} AND chapterName like concat('%', #{idOrName}, '%')")
    int getCourseChapterSectionQuantityBySearch(@Param("teacherId")String teacherId, @Param("courseId") String courseId, @Param("idOrName") String idOrName);


    //编辑章节
//    @Update("update course_chapter_section set chapterSerialNumber=#{chapterSerialNumber},chapterName=#{chapterName},sectionQuantity=#{sectionQuantity} where id=#{id}")
    int updateCourseChapterSectionById(CourseChapterSection courseChapterSection);


    //删除章节
    int deleteCourseChapterSectionByIds(@Param("ids") String[] ids);


}

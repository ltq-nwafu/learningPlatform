package org.sang.mapper;


import org.apache.ibatis.annotations.*;
import org.sang.bean.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CourseMapper {


    //分页
    //显示课程
    //根据teacherId从表course中查询该教师全部课程记录列表，从偏移量offset开始，初始行的偏移量为0，查询记录行数rows，用于分页展示
//    @Select("select * from course where teacherId=#{teacherId} limit #{offset},#{rows}")
    List<Course> getCourseListByTeacherId(@Param("offset") Integer offset, @Param("rows") Integer rows, @Param("teacherId") String teacherId);

    //根据teacherId从表course中查询该教师全部课程记录数量
//    @Select("select count(*) from course where teacherId=#{teacherId}")
    int getCourseQuantityByTeacherId(@Param("teacherId") String teacherId);

    //分页
    //搜索课程
    //根据teacherId和（courseId或courseName)从表course中查询该教师该课程（或课程名相似的课程）记录列表
//    @Select("select * from course where teacherId=#{teacherId} AND (courseId=#{idOrName} or courseName like concat('%', #{idOrName}, '%')) limit #{offset},#{rows}")
    List<Course> getCourseListBySearch(@Param("offset") Integer offset, @Param("rows") Integer rows, @Param("teacherId") String teacherId, @Param("idOrName") String idOrName);

    //根据teacherId和（courseId或courseName)从表course中查询该教师该课程（或课程名相似的课程）记录数量
//    @Select("select count(*) from course where teacherId=#{teacherId} AND (courseId = #{idOrName} or courseName like concat('%', #{idOrName}, '%'))")
    int getCourseQuantityBySearch(@Param("teacherId")String teacherId,@Param("idOrName") String idOrName);


    //添加课程，在表course中插入一条记录
//    @Options(useGeneratedKeys = true)
//    @Insert("insert into course(courseId, courseName, coursePeriod, courseDescription, teacherId) values(#{courseId}, #{courseName}, #{coursePeriod}, #{courseDescription}, #{teacherId})")
    int addCourse(Course course);


    //删除课程
    int deleteCourseByIds(@Param("ids") String[] ids);

    //编辑课程
//    @Update("update course set courseName=#{courseName},coursePeriod=#{coursePeriod},courseDescription=#{courseDescription} where id=#{id}")
    int updateCourseById(Course course);



}

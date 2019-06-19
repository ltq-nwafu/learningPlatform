package org.sang.controller;

import org.sang.bean.CourseChapterSection;
import org.sang.bean.RespBean;
import org.sang.service.CourseChapterSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseChapterSection")
public class CourseChapterSectionController {

    private final CourseChapterSectionService courseChapterSectionService;
    @Autowired
    public CourseChapterSectionController(CourseChapterSectionService courseChapterSectionService) {
        this.courseChapterSectionService = courseChapterSectionService;
    }

    //添加章节
    @RequestMapping(value = "/addCourseChapterSection", method = RequestMethod.POST)
    public RespBean add(CourseChapterSection courseChapterSection) {
        int result = courseChapterSectionService.addCourseChapterSection(courseChapterSection);
        if(result == 1){
            return new RespBean("success", "添加成功！");
        }else{
            return new RespBean("error", "添加失败！");
        }
    }

    //分页
    //显示课程/搜索课程
    @RequestMapping(value = "/courseChapterSectionList", method = RequestMethod.GET)
    public Map<String,Object> getCourseChapterSectionListByTeacherIdCourseId(@RequestParam(value = "pagination",defaultValue = "1") Integer pagination, @RequestParam(value = "rows",defaultValue = "1") Integer rows, @RequestParam(value = "teacherId") String teacherId, @RequestParam(value = "courseId") String courseId, @RequestParam(value = "idOrName") String idOrName) {
        Map<String,Object> map = new HashMap<>();
        List<CourseChapterSection> courseChapterSections;
//        String teacherId = "3";//先写死了teacherId
        if(idOrName == null || idOrName.trim().equals("")){
            courseChapterSections = courseChapterSectionService.getCourseChapterSectionListByTeacherIdCourseId(pagination, rows, teacherId, courseId);
            map.put("courseChapterSections", courseChapterSections);
            map.put("totalCount", courseChapterSectionService.getCourseChapterSectionQuantityByTeacherIdCourseId(teacherId, courseId));
        }else{
            courseChapterSections = courseChapterSectionService.getCourseChapterSectionListBySearch(pagination, rows, teacherId, courseId, idOrName);
            map.put("courseChapterSections", courseChapterSections);
            map.put("totalCount", courseChapterSectionService.getCourseChapterSectionQuantityBySearch(teacherId, courseId, idOrName));
        }
        return map;
    }


    //编辑章节
    @RequestMapping(value = "/editCourseChapterSection", method = RequestMethod.PUT)
    public RespBean updateCourseChapterSection(CourseChapterSection courseChapterSection) {
        int i = courseChapterSectionService.updateCourseChapterSectionById(courseChapterSection);
        if (i == 1) {
            return new RespBean("success", "修改成功!");
        }
        return new RespBean("error", "修改失败!");
    }


    //删除章节
    @RequestMapping(value = "/deleteCourseChapterSection/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteCourseChapterSectionByIds(@PathVariable("ids") String ids) {
        boolean result = courseChapterSectionService.deleteCourseChapterSectionByIds(ids);
        if (result) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }


}

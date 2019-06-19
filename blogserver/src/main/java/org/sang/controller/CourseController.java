package org.sang.controller;


import org.sang.bean.Course;
import org.sang.bean.RespBean;
import org.sang.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseManage")
public class CourseController {

    /*@Autowired
    private CourseService courseService;*/

    private final CourseService courseService;
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }



    //分页
    //显示课程/搜索课程
    @RequestMapping(value = "/courseList", method = RequestMethod.GET)
    public Map<String,Object> getCourseListByTeacherId(@RequestParam(value = "pagination",defaultValue = "1") Integer pagination, @RequestParam(value = "rows",defaultValue = "1") Integer rows, @RequestParam(value = "idOrName") String idOrName) {
        Map<String,Object> map = new HashMap<>();
        List<Course> courses;
        String teacherId = "3";//先写死了teacherId
        if(idOrName == null || idOrName.trim().equals("")){
            courses = courseService.getCourseListByTeacherId(pagination, rows, teacherId);
            map.put("courses", courses);
            map.put("totalCount", courseService.getCourseQuantityByTeacherId(teacherId));
        }else{
            courses = courseService.getCourseListBySearch(pagination, rows, teacherId, idOrName);
            map.put("courses", courses);
            map.put("totalCount", courseService.getCourseQuantityBySearch(teacherId, idOrName));
        }
        return map;
    }


    //添加课程
    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public RespBean add(Course course) {
        int result = courseService.addCourse(course);
        if(result == 1){
            return new RespBean("success", "添加成功！");
        }else{
            return new RespBean("error", "添加失败！");
        }
    }

    //删除课程
    @RequestMapping(value = "/deleteCourse/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteCourseByIds(@PathVariable("ids") String ids) {
        boolean result = courseService.deleteCourseByIds(ids);
        if (result) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    //编辑课程
    @RequestMapping(value = "/editCourse", method = RequestMethod.PUT)
    public RespBean updateCourse(Course course) {
        int i = courseService.updateCourseById(course);
        if (i == 1) {
            return new RespBean("success", "修改成功!");
        }
        return new RespBean("error", "修改失败!");
    }


}

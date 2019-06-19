<template>
  <el-container>
    <el-header class="course_manage_header">
      <el-input
        placeholder="课程编号/课程名"
        prefix-icon="el-icon-search"
        v-model="idOrName"
        style="width: 250px">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="medium" style="margin-left: 5px" @click="searchCourse">搜索</el-button>
      <el-button type="primary" icon="el-icon-circle-plus-outline" size="medium" style="margin-left:750px" @click="addDialogFormVisible = true">添加课程</el-button>

      <!--添加课程使用嵌套表单的Dialog-->
      <el-dialog title="添加课程" :visible.sync="addDialogFormVisible">
        <el-form :model="addCourseForm" :rules="rules" ref="addCourseForm">
          <el-form-item label="课程名称" prop="courseName" :label-width="formLabelWidth">
            <el-input v-model="addCourseForm.courseName" placeholder="课程名称" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="课时" prop="coursePeriod" :label-width="formLabelWidth">
            <el-input v-model="addCourseForm.coursePeriod" placeholder="课时" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="课程描述" prop="courseDescription" :label-width="formLabelWidth">
            <el-input v-model="addCourseForm.courseDescription" placeholder="课程描述" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addDialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addNewCourse">确 定</el-button>
        </div>
      </el-dialog>

    </el-header>

    <el-main class="course_manage_main">
      <!--表格显示课程信息-->
      <el-table
        :data="courses"
        :border=true
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading">
        <el-table-column
          type="selection"
          width="55"
          align="left">
        </el-table-column>
        <el-table-column
          label="序号"
          type="index"
          :index="indexMethod"
          width="50"
          align="left">
        </el-table-column>
        <el-table-column
          label="课程编号"
          prop="courseId"
          width="130"
          align="left">
        </el-table-column>
        <el-table-column
          prop="courseName"
          label="课程名"
          width="180"
          align="left">
        </el-table-column>
        <el-table-column
          label="课时"
          prop="coursePeriod"
          width="50"
          align="left">
        </el-table-column>
        <el-table-column
          label="课程描述"
          prop="courseDescription"
          width="250"
          align="left">
        </el-table-column>
        <el-table-column
          label="教师编号"
          prop="teacherId"
          width="120"
          align="left">
        </el-table-column>
        <el-table-column
          label="操作"
          align="left">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              style="margin-right: 10px"
              @click="showEditDialog(scope.row)">编辑
            </el-button>

            <el-dialog title="编辑课程" :visible.sync="editDialogFormVisible">
              <el-form :model="editCourseForm" :rules="rules" ref="editCourseForm">
                <el-form-item label="课程名称" prop="courseName" :label-width="formLabelWidth">
                  <el-input v-model="editCourseForm.courseName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="课时" prop="coursePeriod" :label-width="formLabelWidth">
                  <el-input v-model="editCourseForm.coursePeriod" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="课程描述" prop="courseDescription" :label-width="formLabelWidth">
                  <el-input v-model="editCourseForm.courseDescription" auto-complete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editCourse">保 存</el-button>
              </div>
            </el-dialog>

            <el-button
              size="mini"
              type="danger"
              @click="deleteOneCourse(scope.$index, scope.row)">删除
            </el-button>

            <el-button
              size="mini"
              type="warning"
              @click="courseChapterSection(scope.row)">章节
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <el-footer class="course_manage_footer">
      <el-button type="danger" :disabled="this.selItems.length === 0" size="medium" style="margin: 0"
                 @click="deleteAll" v-if="this.courses.length>0">批量删除
      </el-button>
      <!--分页-->
      <el-pagination
        background
        :page-size="pageSize"
        :current-page.sync="currentPage"
        :page-sizes="[5, 10, 15, 20]"
        :pager-count="10"
        layout="total,sizes,prev,pager,next"
        :total="totalCount"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        v-if="this.courses.length>0">
      </el-pagination>

    </el-footer>
  </el-container>
</template>
<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'
  import {getRequest} from '../utils/api'
  export default{
    methods: {

      //索引行号/每页当前行号从0开始
      indexMethod(currentRowNumber) {
        return (this.currentPage - 1) * this.pageSize + currentRowNumber + 1;
      },

      //章节管理
      courseChapterSection(row){
        this.$router.push({path: '/courseChapterSection', query: {courseId: row.courseId,teacherId: row.teacherId}});
      },

      //搜索课程
      searchCourse(){
        this.initList(1, this.pageSize, this.idOrName);
      },

      //添加课程
     addNewCourse(){
        this.loading = true;
        let _this = this;
        postRequest('/courseManage/addCourse', {courseName: _this.addCourseForm.courseName, coursePeriod: _this.addCourseForm.coursePeriod, courseDescription: _this.addCourseForm.courseDescription}).then(resp=> {
          if (resp.status === 200) {
            let json = resp.data;
            _this.$message({type: json.status, message: json.msg});
          }
          _this.addDialogFormVisible = false;
          _this.resetAddCourseForm('addCourseForm');
          this.currentPage = this.totalCount / this.pageSize + 1;
          this.initList(this.currentPage, this.pageSize, this.idOrName);
        }, resp=> {
          if (resp.response.status === 403) {
            _this.$message({
              type: 'error',
              message: resp.response.data
            });
          }
          _this.loading = false;
        });

      },
      //清空重置表单
      resetAddCourseForm(formName) {
        this.$refs[formName].resetFields();
      },

      //显示编辑对话框中默认的课程数据
      showEditDialog(row) {
        let _this = this;
        _this.editDialogFormVisible = true;
        _this.editCourseForm.id = row.id;
        _this.editCourseForm.courseName = row.courseName;
        _this.editCourseForm.coursePeriod = row.coursePeriod;
        _this.editCourseForm.courseDescription = row.courseDescription;
      },
      //编辑课程
      editCourse() {
        let _this = this;
        _this.loading = true;
        putRequest("/courseManage/editCourse", {id: this.editCourseForm.id, courseName: this.editCourseForm.courseName, coursePeriod: this.editCourseForm.coursePeriod, courseDescription: this.editCourseForm.courseDescription}).then(resp=> {
          let json = resp.data;
          _this.$message({
            type: json.status,
            message: json.msg
          });
          _this.editDialogFormVisible = false;
          this.initList(this.currentPage, this.pageSize, this.idOrName);
        }, resp=> {
          if (resp.response.status === 403) {
            _this.$message({
              type: 'error',
              message: resp.response.data
            });
          }
          _this.loading = false;
        });
      },

      // 删除一门课程
      deleteOneCourse(index, row){
        let _this = this;
        this.$confirm('确认删除 ' + '《' + row.courseName + '》' + ' ?', '删除课程', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.deleteCourse(row.id);
        }).catch(() => {
          //取消
          _this.loading = false;
        });
      },
      //批量删除
      deleteAll(){
        let _this = this;
        this.$confirm('确认删除这 ' + this.selItems.length + ' 条数据?', '提示', {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(()=> {
          let selItems = _this.selItems;
          let ids = '';
          for (let i = 0; i < selItems.length; i++) {
            ids += selItems[i].id + ",";
          }
          _this.deleteCourse(ids.substring(0, ids.length - 1));
        }).catch(() => {
          //取消
          _this.loading = false;
        });
      },
      handleSelectionChange(val) {
        this.selItems = val;
      },
      //课程删除操作
      deleteCourse(ids){
        let _this = this;
        this.loading = true;
        deleteRequest("/courseManage/deleteCourse/" + ids).then(resp=> {
          let json = resp.data;
          _this.$message({
            type: json.status,
            message: json.msg
          });
          this.initList(this.currentPage, this.pageSize, this.idOrName);
        }, resp=> {
          _this.loading = false;
          if (resp.response.status === 403) {
            _this.$message({
              type: 'error',
              message: resp.response.data
            });
          } else if (resp.response.status === 500) {
            _this.$message({
              type: 'error',
              message: '删除失败!'
            });
          }
        })
      },

      //每页显示条数选择器
      handleSizeChange(val) {
        this.pageSize = val;
        this.loading = true;
        this.initList(this.currentPage, val, this.idOrName);
      },
      //切换页码
      handleCurrentChange(val) {
        this.currentPage = val;
        this.loading = true;
        this.initList(val, this.pageSize, this.idOrName);
      },
      //分页显示
      initList(pagination, rows, idOrName) {
        let _this = this;
        let url = "/courseManage/courseList" + "?pagination=" + pagination + "&rows=" + rows + "&idOrName=" + idOrName;
        getRequest(url).then(resp=> {
          _this.loading = false;
          if (resp.status === 200) {
            _this.courses = resp.data.courses;
            _this.totalCount = resp.data.totalCount;
          }else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp=> {
          _this.loading = false;
          if (resp.response.status === 403) {
            _this.$message({type: 'error', message: resp.response.data});
          }/* else {
            _this.$message({type: 'error', message: 'Not 403!'});
          }*/
        }).catch(resp=> {
          //压根没见到服务器
          _this.loading = false;
          _this.$message({type: 'error', message: '无法连接服务器!'});
        })
      }
    },
    mounted: function () {
      this.loading = true;
      // 登录后默认显示课程表格
      this.initList(1, this.pageSize, this.idOrName);
      /*let _this = this;
      window.bus.$on('courseListReload', function () {
        _this.loading = true;
        _this.initList(_this.currentPage, _this.pageSize, _this.idOrName);
      })*/
    },
    data(){
      return {
        idOrName: '',
        pageSize: 5,
        totalCount: 50,
        currentPage: 1,
        selItems: [],
        courses: [],
        loading: false,
        addDialogFormVisible: false,
        addCourseForm: {
          courseName: '',
          coursePeriod: '',
          courseDescription: '',
        },
        editDialogFormVisible: false,
        editCourseForm: {
          id: '',
          courseName: '',
          coursePeriod: '',
          courseDescription: '',
        },
        rules: {
          courseName: [
            { required: true, message: '请输入课程名称', trigger: 'blur' }
            // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          coursePeriod: [
            { required: true, message: '请输入课时', trigger: 'blur' }
          ],
          courseDescription: [
            { required: true, message: '请输入课程描述', trigger: 'blur' }
          ]
        },
        formLabelWidth: '120px'
      }
    },
    watch: {
      totalCount: function() {
        if(this.totalCount === (this.currentPage - 1) * this.pageSize && this.totalCount !== 0){
          this.currentPage -= 1;
          this.initList(this.currentPage, this.pageSize, this.idOrName);
        }
        if(this.totalCount === (this.currentPage * this.pageSize + 1)){
          this.currentPage += 1;
          this.initList(this.currentPage, this.pageSize, this.idOrName);
        }
      }
    }
  }
</script>
<style>
  .course_manage_header {
    background-color: #ececec;
    margin-top: 20px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .course_manage_main {
    display: flex;
    flex-direction: column;
    padding-left: 5px;
    background-color: #ececec;
    margin-top: 20px;
    padding-top: 10px;
  }

  .course_manage_footer {
    display: flex;
    padding-top: 10px;
    padding-left: 0;
    padding-bottom: 15px;
    justify-content: space-between;
  }
</style>

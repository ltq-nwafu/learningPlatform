<template>
  <el-container>
    <el-header class="chapter_section_header">
      <el-input
        placeholder="章名"
        prefix-icon="el-icon-search"
        v-model="idOrName"
        style="width: 250px">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="medium" style="margin-left: 5px" @click="searchCCS">搜索</el-button>
      <el-button type="primary" icon="el-icon-circle-plus-outline" size="medium" align="right" style="margin-left:750px;" @click="addDialogFormVisible = true">添加</el-button>
      <el-dialog title="添加章节" :visible.sync="addDialogFormVisible">
        <el-form :model="addCCSForm" :rules="rules" ref="addCCSForm">
          <el-form-item label="章序" prop="chapterSerialNumber" :label-width="formLabelWidth">
            <el-input v-model="addCCSForm.chapterSerialNumber" placeholder="章序" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="章名" prop="chapterName" :label-width="formLabelWidth">
            <el-input v-model="addCCSForm.chapterName" placeholder="章名" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="节数" prop="sectionQuantity" :label-width="formLabelWidth">
            <el-input v-model="addCCSForm.sectionQuantity" placeholder="节数" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addDialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addNewCCS">确 定</el-button>
        </div>
      </el-dialog>
    </el-header>

    <el-main class="chapter_section_main">
      <!--表格显示章节信息-->
      <el-table
        :data="courseChapterSections"
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
        <!--<el-table-column
          label="课程名"
          prop="courseName"
          width="250"
          align="left">
        </el-table-column>-->
        <el-table-column
          label="章序"
          prop="chapterSerialNumber"
          width="150"
          align="left">
        </el-table-column>
        <el-table-column
          label="章名"
          prop="chapterName"
          width="250"
          align="left">
        </el-table-column>
        <el-table-column
          label="节数"
          prop="sectionQuantity"
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

            <el-dialog title="编辑章节" :visible.sync="editDialogFormVisible">
              <el-form :model="editCCSForm" :rules="rules" ref="editCCSForm">
                <el-form-item label="章序" prop="courseName" :label-width="formLabelWidth">
                  <el-input v-model="editCCSForm.chapterSerialNumber" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="章名" prop="coursePeriod" :label-width="formLabelWidth">
                  <el-input v-model="editCCSForm.chapterName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="节数" prop="courseDescription" :label-width="formLabelWidth">
                  <el-input v-model="editCCSForm.sectionQuantity" auto-complete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editCCS">保 存</el-button>
              </div>
            </el-dialog>

            <el-button
              size="mini"
              type="danger"
              @click="deleteOneCCS(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <el-footer class="chapter_section_footer">
      <el-row type="flex" justify="space-between">
        <el-col :span="1">
          <div>
            <el-button type="danger" :disabled="this.selItems.length === 0" size="medium"
                       @click="deleteAll" v-if="this.courseChapterSections.length>0">批量删除
            </el-button>
          </div>
        </el-col>

        <el-col :span="6">
          <div>
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
              v-if="this.courseChapterSections.length>0">
            </el-pagination>
          </div>
        </el-col>
      </el-row>

      <el-row >
        <el-col><div><el-button type="primary" icon="el-icon-back" size="medium" @click="goBack" style="margin-top: 10px">返回</el-button></div></el-col>
      </el-row>
    </el-footer>
  </el-container>
</template>

<script>
    import {postRequest} from '../utils/api'
    import {putRequest} from '../utils/api'
    import {deleteRequest} from '../utils/api'
    import {getRequest} from '../utils/api'
    export default {
      methods: {

        //索引行号
        indexMethod(currentRowNumber) {
          return (this.currentPage - 1) * this.pageSize + currentRowNumber + 1;
        },

        //返回课程管理
        goBack(){
          this.$router.go(-1);
        },

        //搜索章节
        searchCCS(){
          this.initList(1, this.pageSize, this.idOrName);
        },

        //添加章节
        addNewCCS(){
          let courseId = this.$route.query.courseId;
          let teacherId = this.$route.query.teacherId;
          this.loading = true;
          let _this = this;
          postRequest('/courseChapterSection/addCourseChapterSection', {chapterSerialNumber: this.addCCSForm.chapterSerialNumber, chapterName: this.addCCSForm.chapterName, sectionQuantity: this.addCCSForm.sectionQuantity, courseId: courseId, teacherId: teacherId}).then(resp=> {
            if (resp.status === 200) {
              let json = resp.data;
              _this.$message({type: json.status, message: json.msg});
            }
            _this.addDialogFormVisible = false;
            _this.resetCCSForm('addCCSForm');
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
        resetCCSForm(formName) {
          this.$refs[formName].resetFields();
        },

        //显示编辑对话框中默认的章节数据
        showEditDialog(row) {
          let _this = this;
          _this.editDialogFormVisible = true;
          _this.editCCSForm.id = row.id;
          _this.editCCSForm.chapterName = row.chapterName;
          _this.editCCSForm.chapterSerialNumber = row.chapterSerialNumber;
          _this.editCCSForm.sectionQuantity = row.sectionQuantity;
        },

        //编辑章节
        editCCS() {
          let _this = this;
          _this.loading = true;
          putRequest("/courseChapterSection/editCourseChapterSection", {id: this.editCCSForm.id, chapterName: this.editCCSForm.chapterName, chapterSerialNumber: this.editCCSForm.chapterSerialNumber, sectionQuantity: this.editCCSForm.sectionQuantity}).then(resp=> {
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

        // 删除一条章节
        deleteOneCCS(index, row){
          let _this = this;
          this.$confirm('确认删除 ' + row.chapterName + ' ?', '删除章节', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            _this.deleteCCS(row.id);
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
            _this.deleteCCS(ids.substring(0, ids.length - 1));
          }).catch(() => {
            //取消
            _this.loading = false;
          });
        },
        //选中
        handleSelectionChange(val) {
          this.selItems = val;
        },
        //章节删除操作
        deleteCCS(ids){
          let _this = this;
          this.loading = true;
          deleteRequest("/courseChapterSection/deleteCourseChapterSection/" + ids).then(resp=> {
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
          let courseId = this.$route.query.courseId;
          let teacherId = this.$route.query.teacherId;
          // alert(courseId);
          let _this = this;
          let url = "/courseChapterSection/courseChapterSectionList" + "?pagination=" + pagination + "&rows=" + rows + "&teacherId=" + teacherId + "&courseId=" + courseId + "&idOrName=" + idOrName;
          getRequest(url).then(resp=> {
            _this.loading = false;
            if (resp.status === 200) {
              _this.courseChapterSections = resp.data.courseChapterSections;
              _this.totalCount = resp.data.totalCount;
            }else {
              _this.$message({type: 'error', message: '数据加载失败!'});
            }
          }, resp=> {
            _this.loading = false;
            if (resp.response.status === 403) {
              _this.$message({type: 'error', message: resp.response.data});
            } else {
              _this.$message({type: 'error', message: 'Not 403!'});
            }
          }).catch(resp=> {
            //压根没见到服务器
            _this.loading = false;
            _this.$message({type: 'error', message: '无法连接服务器!'});
          })
        }
      },
      mounted: function () {
        this.loading = true;
        // 登录后默认显示章节表格
        this.initList(1, this.pageSize, this.idOrName);
      },
      data(){
        return {
          idOrName: '',
          pageSize: 5,
          totalCount: 50,
          currentPage: 1,
          selItems: [],
          courseChapterSections: [],
          loading: false,
          addDialogFormVisible: false,
          addCCSForm: {
            chapterSerialNumber: '',
            chapterName: '',
            sectionQuantity: '',
          },
          editDialogFormVisible: false,
          editCCSForm: {
            id: '',
            chapterSerialNumber: '',
            chapterName: '',
            sectionQuantity: '',
          },
          rules: {
            chapterSerialNumber: [
              { required: true, message: '请输入章序号', trigger: 'blur' }
              // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
            ],
            chapterName: [
              { required: true, message: '请输入章名', trigger: 'blur' }
            ],
            sectionQuantity: [
              { required: true, message: '请输入节数', trigger: 'blur' }
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
  .chapter_section_header {
    background-color: #ececec;
    margin-top: 20px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .chapter_section_main {
    display: flex;
    flex-direction: column;
    padding-left: 5px;
    background-color: #ececec;
    margin-top: 20px;
    padding-top: 10px;
  }

  .chapter_section_footer {
    padding-top: 10px;
    padding-left: 0;
  }

</style>

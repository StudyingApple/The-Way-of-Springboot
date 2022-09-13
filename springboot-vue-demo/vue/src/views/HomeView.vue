<template>

  <div style="padding: 10px;">

    <div style="margin: 10px 0; display: flex">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>

    <div style="margin: 10px 0; display: flex">
      <el-input v-model="search" placeholder="输入关键字" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="name" label="用户名" sortable/>
      <el-table-column prop="nickName" label="昵称" sortable/>
      <el-table-column prop="age" label="年龄" sortable/>
      <el-table-column prop="sex" label="性别" sortable>
        <template #default="scope">
          <span v-if="scope.row.sex=== 1">男</span>
          <span v-if="scope.row.sex=== 2">女</span>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址"/>
      <el-table-column fixed="right" label="Operations" width="120">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)"
          >编辑
          </el-button
          >
          <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button link type="primary" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5,10,20]"
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog v-model="dialogVisible" title="Tips" width="40%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="用户名" style="width: 80%">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="昵称" style="width: 80%">
          <el-input v-model="form.nickName" />
        </el-form-item>
        <el-form-item label="密码" style="width: 80%">
          <el-input v-model="form.password" />
        </el-form-item>
        <el-form-item label="年龄" style="width: 80%">
          <el-input v-model="form.age" />
        </el-form-item>

        <el-form-item label="性別" style="width: 80%">
          <el-radio v-model="form.sex" label="1" size="large">男</el-radio>
          <el-radio v-model="form.sex" label="2" size="large">女</el-radio>
          <el-radio v-model="form.sex" label="3" size="large">未知</el-radio>
        </el-form-item>

        <el-form-item label="地址" style="width: 80%">
          <el-input v-model="form.address" type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">
          确定
        </el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request"

export default {
  name: 'HomeView',
  components: {},
  data() {
    return {
      dialogVisible: false,
      form: {},
      currentPage: 1,
      pageSize: 10,
      search: '',
      tableData: [
      ]
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get("/api/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          condition: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.form = {}
      this.dialogVisible = true
    },
    save() {
      if (this.form.id) {
        request.put("/api/user", this.form).then(res => {
          console.log(res)
          this.dialogVisible = false
          this.load()
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          }
        })
      } else {
        request.post("/api/user", this.form).then(res => {
          console.log(res)
          this.dialogVisible = false
          this.load()
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "err",
              message: res.msg
            })
          }
        })
      }
    },
    handleDelete(id) {
      request.delete("/api/user/" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "err",
            message: res.msg
          })
        }
        this.load()
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSizeChange() {
      this.load()
    },
    handleCurrentChange() {
      this.load()
    }
  }
}
</script>

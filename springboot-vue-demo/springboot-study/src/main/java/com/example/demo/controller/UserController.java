package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;

    @PostMapping
    public Result<?> save(@RequestBody User user) {

        if ((user.getPassword() == null) || (user.getName() == null)) {
            return Result.error("1", "参数错误");
        }

        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    @GetMapping
    public Result<Page<User>> get(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String condition) {

        Page<User> userPage = userMapper.selectPage(new Page<User>(pageNum, pageSize), Wrappers.<User>lambdaQuery().like(User::getNickName, condition));
        return Result.success(userPage);
    }

    @DeleteMapping("/{id}")
    public Result<?> del(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }
}

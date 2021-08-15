package com.ppphuang.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ppphuang.demo.dao.mappers.test.UserPOMapper;
import com.ppphuang.demo.dao.model.test.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class MybatisController {

    UserPOMapper userPOMapper;

    @Autowired
    public void setUserPOMapper(UserPOMapper userPOMapper) {
        this.userPOMapper = userPOMapper;
    }

    @GetMapping(value = "user/get")
    public Object getUser() {
        UserPO userPO = userPOMapper.selectByPrimaryKey(1);
        return userPO;
    }

    @GetMapping(value = "user/get/page")
    public Object getUserPage() {
        HashMap<String, Object> searchMap = new HashMap<>();
        searchMap.put("id", 1);
        searchMap.put("name", "张三");
        Page page = PageHelper.startPage(1, 1);
        List<UserPO> userPOS = userPOMapper.selectByMapRight(searchMap);
        HashMap<String, Object> res = new HashMap<>();
        res.put("count", page.getTotal());
        res.put("size", page.getPageSize());
        res.put("page", page.getPageNum());
        res.put("result", userPOS);
        return res;
    }

    @GetMapping(value = "user/where/wrong")
    public Object getUserWhereWrong() {
        HashMap<String, Object> searchMap = new HashMap<>();
        searchMap.put("id", 1);
        searchMap.put("name", "张三");
        List<UserPO> userPOS = userPOMapper.selectByMapWrong(searchMap);
        searchMap.clear();
        searchMap.put("name", "张三");
        userPOS = userPOMapper.selectByMapWrong(searchMap);
        return userPOS;
    }

    @GetMapping(value = "user/where/right")
    public Object getUserWhereRight() {
        HashMap<String, Object> searchMap = new HashMap<>();
        searchMap.put("id", 1);
        searchMap.put("name", "张三");
        List<UserPO> userPOS = userPOMapper.selectByMapRight(searchMap);
        searchMap.clear();
        searchMap.put("name", "张三");
        userPOS = userPOMapper.selectByMapRight(searchMap);
        return userPOS;
    }
}

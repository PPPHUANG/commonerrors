package com.ppphuang.demo.dao.mappers.test;

import com.ppphuang.demo.dao.model.test.UserPO;

import java.util.HashMap;
import java.util.List;

public interface UserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);

    List<UserPO> selectByMapWrong(HashMap<String, Object> map);

    List<UserPO> selectByMapRight(HashMap<String, Object> map);

    List<UserPO> selectByMapUnion(HashMap<String, Object> map);
}
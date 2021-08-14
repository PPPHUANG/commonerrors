package com.ppphuang.demo.dao.mappers.test;

import com.ppphuang.demo.dao.model.test.UserPO;

public interface UserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);
}
package com.ppphuang.demo.dao.mappers.test;

import com.ppphuang.demo.dao.model.test.AddressPO;

public interface AddressPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AddressPO record);

    int insertSelective(AddressPO record);

    AddressPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AddressPO record);

    int updateByPrimaryKey(AddressPO record);
}
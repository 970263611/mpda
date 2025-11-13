package com.dahuaboke.mpda.bot.tools.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.entity.FdIntbankKindpara;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface FdIntbankKindparaMapper extends BaseMapper<FdIntbankKindpara> {
    @DS("db1")
    List<String> selectFundCodes(@Param("today") String today, @Param("start") Integer start, @Param("count") Integer count);
}
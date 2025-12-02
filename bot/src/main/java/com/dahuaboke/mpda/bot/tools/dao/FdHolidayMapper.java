package com.dahuaboke.mpda.bot.tools.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.dahuaboke.mpda.bot.tools.entity.FdHoliday;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: 节假日
 * @author: ZHANGSHUHAN
 * @date: 2025/10/29
 */
@Mapper
public interface FdHolidayMapper {

    @DS("db1")
    @Select("select thedate,fundworkday,financeworkday,agentworkday,cdworkday,sdworkday,last_mod_stamp " +
            "from fd_holiday where (thedate like #{year} and fundworkday = '1')")
    List<FdHoliday> selectList(@Param("year") String year);
}

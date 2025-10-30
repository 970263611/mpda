package com.dahuaboke.mpda.bot.tools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.tools.dto.BrNetvalueDto;
import com.dahuaboke.mpda.bot.tools.entity.BrNetvalue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: 通过储蓄账号查询用户信息
 * @Description:
 * @author: 吕远征
 * @date: 2022/2/28 16:13
 */
@Mapper
public interface BrNetvalueMapper extends BaseMapper<BrNetvalue> {
    /**
     * 最新净值查询
     * @param fundCodes
     * @return
     */
    public List<BrNetvalueDto> selectBrNetvalueLatest(@Param("fundCodes") List<String> fundCodes);

}
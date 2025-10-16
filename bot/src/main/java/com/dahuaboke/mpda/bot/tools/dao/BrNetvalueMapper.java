package com.dahuaboke.mpda.bot.tools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrNetvalue;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: 通过储蓄账号查询用户信息
 * @Description:
 * @author: 吕远征
 * @date: 2022/2/28 16:13
 */
@Mapper
public interface BrNetvalueMapper extends BaseMapper<BrNetvalue> {

}
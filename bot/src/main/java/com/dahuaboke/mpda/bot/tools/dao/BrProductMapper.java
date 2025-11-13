package com.dahuaboke.mpda.bot.tools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.tools.dto.BrNetvalueDto;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface BrProductMapper extends BaseMapper<BrProduct> {
    List<RecommendProductDto> selectFundDetail(@Param("fundCodes") List<String> fundCodes);

}
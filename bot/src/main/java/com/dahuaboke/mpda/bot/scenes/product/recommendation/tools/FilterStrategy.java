package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;

import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import java.util.List;

interface FilterStrategy {

    List<RecommendProductDto> handle(List<RecommendProductDto> filterInfoList, FilterContext filterContext);

}

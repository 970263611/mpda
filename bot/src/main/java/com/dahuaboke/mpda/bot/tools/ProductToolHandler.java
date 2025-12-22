package com.dahuaboke.mpda.bot.tools;


import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import com.dahuaboke.mpda.bot.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/26 17:06
 */
public class ProductToolHandler {

    @Autowired
    private RobotService robotService;

    /**
     * 查产品信息
     *
     * @param netValReq
     * @return ProdInfoDto
     */
    public ProdInfoDto selectProdInfo(NetValReq netValReq) {
        return robotService.getInfo(netValReq.getProdtCode());
    }

    /**
     * 映射产品代码与名称
     *
     * @return Map
     */
    public Map<String, String> getMap() {
        return robotService.getMap();
    }


    /**
     * 最大回撤
     *
     * @param netValReq
     * @return
     */
    public String maxWithDrawal(NetValReq netValReq) {
        if (StringUtils.isEmpty(netValReq.getEndDate())) {
            netValReq.setEndDate(DateUtil.getBusinessToday());
        }
        if (StringUtils.isEmpty(netValReq.getBegDate())) {
            String txDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -90);
            netValReq.setBegDate(txDate);
        }
        String withDrawal = robotService.getWithDrawal(netValReq);
        BigDecimal divide = new BigDecimal(withDrawal);
        return divide.multiply(new BigDecimal(100), new MathContext(5, RoundingMode.HALF_UP)) + "%";
    }

    /**
     * 年收益率
     *
     * @param netValReq
     * @return
     */
    public String yearRita(NetValReq netValReq) {
        if (StringUtils.isEmpty(netValReq.getEndDate())) {
            netValReq.setEndDate(DateUtil.getBusinessToday());
        }
        if (StringUtils.isEmpty(netValReq.getBegDate())) {
            String txDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30);
            netValReq.setBegDate(txDate);
        }
        String netVal = robotService.yearRita(netValReq);
        BigDecimal divide = new BigDecimal(netVal);
        String tempAns = divide.multiply(new BigDecimal(100), new MathContext(5, RoundingMode.HALF_UP)).toString();
        return tempAns + "%";
    }

    /**
     * 货基七日年化
     *
     * @param netValReq
     * @return
     */
    public String sevenDayYearlyProfrat(NetValReq netValReq) {
        return robotService.sevenDayYearlyProfrat(netValReq);
    }


    /**
     * 货基万份收益
     * @param netValReq
     * @return
     */
    public String thouCopFundUnitProfit(NetValReq netValReq) {
        return robotService.thouCopFundUnitProfit(netValReq);
    }

    /**
     * 市场排名报告
     * @param finBondType
     * @param period
     * @return
     */
    public List<MarketRankDto> getMarketRank(String finBondType, String period) {
        return robotService.getMarketRank(finBondType, period);
    }


    public List<RecommendProductDto> getFundInfoByType(String fundType) {
        return robotService.getFundInfoByType(fundType);
    }

    /**
     * 获取最新季度时间
     * @param localDate
     * @return
     */
    public String dealDldFlnm(LocalDate localDate) {
        return robotService.dealDldFlnm(localDate);
    }
}

package com.dahuaboke.mpda.bot.tools;


import com.dahuaboke.mpda.bot.tools.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import com.dahuaboke.mpda.bot.utils.DateUtil;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
    public Map<String, List<String>> getMap() {
        return robotService.getMap();
    }

    /**
     * 根据输入条件筛选产品
     *
     * @param filterProdInfoReq
     * @return
     */
    public List<ProdInfoDto> filterProdInfo(FilterProdInfoReq filterProdInfoReq) {
        return robotService.filterProdInfo(filterProdInfoReq);
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


    public List<MarketRankDto> getMarketRank(String finBondType,String period) {
        return robotService.getMarketRank(finBondType,period);
    }


    protected List<ProdInfoDto> getFundByType(String fundType) {
        return robotService.getFundByType(fundType);
    }

}

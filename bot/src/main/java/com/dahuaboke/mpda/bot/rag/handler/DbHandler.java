package com.dahuaboke.mpda.bot.rag.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrMarketProductReportMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductReportMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductSummaryMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbHandler {

    @Autowired
    BrProductMapper brProductMapper;

    @Autowired
    BrProductReportMapper brProductReportMapper;

    @Autowired
    BrProductSummaryMapper brProductSummaryMapper;

    @Autowired
    BrMarketProductReportMapper brMarketProductReportMapper;


    /**
     * 原子性标记并查询待处理数据
     *
     * @param maxCount 查询条数
     * @return 查询数据
     */
    public List<BrProduct> markAndSelectUnprocessed(int maxCount) {
        //获取基金代码
        LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                .eq(BrProduct::getDealFlag, FileDealFlag.UNPROCESSED.getCode())
                .orderByAsc(BrProduct::getFundCode)
                .last("LIMIT " + maxCount);
        List<BrProduct> brProducts = brProductMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(brProducts)) {
            return List.of();
        }

        brProducts.forEach(brProduct -> {
            LambdaUpdateWrapper<BrProduct> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BrProduct::getFundCode, brProduct.getFundCode()).eq(BrProduct::getAncmTpBclsCd, brProduct.getAncmTpBclsCd());
            brProduct.setDealFlag(FileDealFlag.PROCESSING.getCode());
            brProductMapper.update(brProduct, updateWrapper);
        });

        return brProducts;
    }

    public int resetTimeout() {
        LambdaUpdateWrapper<BrProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProduct::getDealFlag, FileDealFlag.PROCESSING.getCode())
                .isNotNull(BrProduct::getDealBgnTime);
        updateWrapper.apply("EXTRACT(EPOCH FROM (NOW() - deal_bgn_time)) > tmout_time_num");

        updateWrapper.set(BrProduct::getDealFlag, FileDealFlag.UNPROCESSED.getCode())
                .set(BrProduct::getDealBgnTime, null)
                .set(BrProduct::getTmoutTimeNum, null);

        return brProductMapper.update(null, updateWrapper);

    }

    public int updateFailProduct() {
        LambdaUpdateWrapper<BrProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProduct::getDealFlag, FileDealFlag.PROCESS_FAIL.getCode());

        updateWrapper.set(BrProduct::getDealFlag, FileDealFlag.UNPROCESSED.getCode())
                .set(BrProduct::getDealBgnTime, null)
                .set(BrProduct::getTmoutTimeNum, null);

        return brProductMapper.update(null, updateWrapper);

    }


    public List<BrProductReport> selectBrProductReport(BrProductReport brProductReport) {
        LambdaQueryWrapper<BrProductReport> queryWrapper = new LambdaQueryWrapper<BrProductReport>()
                .eq(BrProductReport::getFundCode, brProductReport.getFundCode());
        return brProductReportMapper.selectList(queryWrapper);
    }

    public List<BrProductSummary> selectBrProductSummary(BrProductSummary brProductSummary) {
        LambdaQueryWrapper<BrProductSummary> queryWrapper = new LambdaQueryWrapper<BrProductSummary>()
                .eq(BrProductSummary::getFundCode, brProductSummary.getFundCode());
        return brProductSummaryMapper.selectList(queryWrapper);
    }


    public void updateProduct(BrProduct brProduct) {
        LambdaUpdateWrapper<BrProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProduct::getFundCode, brProduct.getFundCode()).eq(BrProduct::getAncmTpBclsCd, brProduct.getAncmTpBclsCd());
        brProductMapper.update(brProduct, updateWrapper);
    }

    public void updateProductReport(BrProductReport brProductReport) {
        LambdaUpdateWrapper<BrProductReport> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProductReport::getFundCode, brProductReport.getFundCode());
        brProductReportMapper.update(brProductReport, updateWrapper);
    }

    public void updateProductSummary(BrProductSummary brProductSummary) {
        LambdaUpdateWrapper<BrProductSummary> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProductSummary::getFundCode, brProductSummary.getFundCode());
        brProductSummaryMapper.update(brProductSummary, updateWrapper);
    }

    public void insertProductReport(BrProductReport brProductReport) {
        LambdaQueryWrapper<BrProductReport> queryWrapper = new LambdaQueryWrapper<BrProductReport>()
                .eq(BrProductReport::getFundCode, brProductReport.getFundCode());
        List<BrProductReport> list = brProductReportMapper.selectList(queryWrapper);

        if (list != null && list.size() > 0) {
            LambdaUpdateWrapper<BrProductReport> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BrProductReport::getFundCode, brProductReport.getFundCode());
            brProductReportMapper.update(brProductReport, updateWrapper);
        } else {
            brProductReportMapper.insert(brProductReport);
        }
    }

    public void insertProductSummary(BrProductSummary brProductSummary) {
        LambdaQueryWrapper<BrProductSummary> queryWrapper = new LambdaQueryWrapper<BrProductSummary>()
                .eq(BrProductSummary::getFundCode, brProductSummary.getFundCode());
        List<BrProductSummary> list = brProductSummaryMapper.selectList(queryWrapper);

        if (list != null && list.size() > 0) {
            LambdaUpdateWrapper<BrProductSummary> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BrProductSummary::getFundCode, brProductSummary.getFundCode());
            brProductSummaryMapper.update(brProductSummary, updateWrapper);
        } else {
            brProductSummaryMapper.insert(brProductSummary);
        }
    }

    public List<BrMarketProductReport> selectAllMarketProductReport() {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>();
        return brMarketProductReportMapper.selectList(queryWrapper);
    }

    public void insertMarketProductReport(BrMarketProductReport brMarketProductReport) {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>()
                .eq(BrMarketProductReport::getFundCode, brMarketProductReport.getFundCode());
        List<BrMarketProductReport> list = brMarketProductReportMapper.selectList(queryWrapper);

        if (list != null && list.size() > 0) {
            LambdaUpdateWrapper<BrMarketProductReport> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BrMarketProductReport::getFundCode, brMarketProductReport.getFundCode());
            brMarketProductReportMapper.update(brMarketProductReport, updateWrapper);
        } else {
            brMarketProductReportMapper.insert(brMarketProductReport);
        }
    }
}

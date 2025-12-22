package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import com.dahuaboke.mpda.bot.tools.enums.FundInfoType;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrProductService {

    @Autowired
    private BrProductMapper brProductMapper;

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

    public BrProduct selectBrProductById(String fundCode,String ancmTpBclsCd){
        LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                .eq(BrProduct::getFundCode,fundCode)
                .eq(BrProduct::getAncmTpBclsCd,ancmTpBclsCd);
        List<BrProduct> brProducts = brProductMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(brProducts)) {
            return new BrProduct();
        }
        return brProducts.get(0);
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

    public void updateProduct(BrProduct brProduct) {
        LambdaUpdateWrapper<BrProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProduct::getFundCode, brProduct.getFundCode()).eq(BrProduct::getAncmTpBclsCd, brProduct.getAncmTpBclsCd());
        brProductMapper.update(brProduct, updateWrapper);
    }

    public int updateProcessingProduct(String dealFlag) {
        LambdaUpdateWrapper<BrProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProduct::getDealFlag, dealFlag);

        updateWrapper.set(BrProduct::getDealFlag, FileDealFlag.UNPROCESSED.getCode())
                .set(BrProduct::getDealBgnTime, null)
                .set(BrProduct::getTmoutTimeNum, null);

        return brProductMapper.update(null, updateWrapper);

    }

    /**
     * 获取债基类型的基金代码
     * @param fundType
     * @return
     */
    public List<String> getRateFundCode(String fundType) {
        LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                .select(BrProduct::getFundCode)
                .eq(BrProduct::getProdtClsCode, fundType)
                .and(w -> w.eq(BrProduct::getAncmTpBclsCd, FundInfoType.SUMMARY.getCode())
                        .or(w2 ->w2.eq(BrProduct::getAncmTpBclsCd, FundInfoType.REPORT.getCode())
                                .ge(BrProduct::getDlineDate, "20241231")))
                .groupBy(BrProduct::getFundCode)
                .having("count(1) = {0}" ,2);
        return brProductMapper.selectObjs(queryWrapper).stream().map(Object::toString).distinct().toList();
    }



}

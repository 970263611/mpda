package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductSummaryMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrProductSummaryService {

    @Autowired
    private BrProductSummaryMapper brProductSummaryMapper;

    public List<BrProductSummary> selectBrProductSummary(BrProductSummary brProductSummary) {
        LambdaQueryWrapper<BrProductSummary> queryWrapper = new LambdaQueryWrapper<BrProductSummary>()
                .eq(BrProductSummary::getFundCode, brProductSummary.getFundCode());
        return brProductSummaryMapper.selectList(queryWrapper);
    }

    public List<BrProductSummary> selectBrProductSummaryByCodes(List<String> fundCodes) {
        LambdaQueryWrapper<BrProductSummary> queryWrapper = new LambdaQueryWrapper<BrProductSummary>()
                .in(BrProductSummary::getFundCode, fundCodes);
        return brProductSummaryMapper.selectList(queryWrapper);
    }


    public void updateProductSummary(BrProductSummary brProductSummary) {
        LambdaUpdateWrapper<BrProductSummary> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProductSummary::getFundCode, brProductSummary.getFundCode());
        brProductSummaryMapper.update(brProductSummary, updateWrapper);
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

}

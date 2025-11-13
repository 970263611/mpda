package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrMarketProductReportMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrMarketProductReportService {

    @Autowired
    private BrMarketProductReportMapper brMarketProductReportMapper;


    public List<BrMarketProductReport> selectAllMarketProductReport() {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>();
        return brMarketProductReportMapper.selectList(queryWrapper);
    }


    /**
     * 获取市场报告表的基金代码
     *
     * @return
     */
    public Set<String> getMarketProductCode() {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>()
                .select(BrMarketProductReport::getFundCode);

        return brMarketProductReportMapper.selectObjs(queryWrapper).stream().map(Object::toString).collect(Collectors.toSet());
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

    public void delMarketProductByCode(String fundCode) {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>()
                .eq(BrMarketProductReport::getFundCode, fundCode);
        brMarketProductReportMapper.delete(queryWrapper);
    }

}

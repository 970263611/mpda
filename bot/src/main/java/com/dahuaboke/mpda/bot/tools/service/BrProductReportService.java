package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductReportMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrProductReportService {

    @Autowired
    BrProductReportMapper brProductReportMapper;

    public List<BrProductReport> selectBrProductReport(BrProductReport brProductReport) {
        LambdaQueryWrapper<BrProductReport> queryWrapper = new LambdaQueryWrapper<BrProductReport>()
                .eq(BrProductReport::getFundCode, brProductReport.getFundCode());
        return brProductReportMapper.selectList(queryWrapper);
    }

    public List<BrProductReport> selectBrProductReportByCodes(List<String> fundCodes) {
        LambdaQueryWrapper<BrProductReport> queryWrapper = new LambdaQueryWrapper<BrProductReport>()
                .in(BrProductReport::getFundCode,fundCodes);
        return brProductReportMapper.selectList(queryWrapper);
    }

    public void updateProductReport(BrProductReport brProductReport) {
        LambdaUpdateWrapper<BrProductReport> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrProductReport::getFundCode, brProductReport.getFundCode());
        brProductReportMapper.update(brProductReport, updateWrapper);
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

}

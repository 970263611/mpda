package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrPdfParseExceptionsMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrPdfParseExceptions;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrPdfParseExceptionsService {

    @Autowired
    public BrPdfParseExceptionsMapper brPdfParseExceptionsMapper;


    public void insertParseException(BrPdfParseExceptions brPdfParseExceptions) {
        brPdfParseExceptionsMapper.insert(brPdfParseExceptions);
    }

    public void updateParseException(BrPdfParseExceptions brPdfParseExceptions) {
        LambdaUpdateWrapper<BrPdfParseExceptions> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrPdfParseExceptions::getId, brPdfParseExceptions.getId());
        brPdfParseExceptionsMapper.update(brPdfParseExceptions, updateWrapper);
    }

    public void delParseException(BrPdfParseExceptions brPdfParseExceptions) {
        LambdaQueryWrapper<BrPdfParseExceptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BrPdfParseExceptions::getId, brPdfParseExceptions.getId());
        brPdfParseExceptionsMapper.delete(queryWrapper);
    }


    public List<BrPdfParseExceptions> queryAllParseException() {
        LambdaQueryWrapper<BrPdfParseExceptions> queryWrapper = new LambdaQueryWrapper<BrPdfParseExceptions>()
                .select(BrPdfParseExceptions::getId,
                        BrPdfParseExceptions::getFundCode,
                        BrPdfParseExceptions::getAncmTpBclsCd,
                        BrPdfParseExceptions::getFundProdtFullNm,
                        BrPdfParseExceptions::getExceptionType,
                        BrPdfParseExceptions::getQuestion,
                        BrPdfParseExceptions::getCount,
                        BrPdfParseExceptions::getStatus)
                .eq(BrPdfParseExceptions::getStatus, FileDealFlag.PROCESS_FAIL.getCode())
                .lt(BrPdfParseExceptions::getCount, 3)
                .last("LIMIT " + 300);
        return brPdfParseExceptionsMapper.selectList(queryWrapper);
    }

    public int updateCount() {
        LambdaUpdateWrapper<BrPdfParseExceptions> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrPdfParseExceptions::getCount, 3);

        updateWrapper.set(BrPdfParseExceptions::getStatus, FileDealFlag.PROCESS_FAIL.getCode())
                .set(BrPdfParseExceptions::getCount, 0);

        return brPdfParseExceptionsMapper.update(null, updateWrapper);

    }
}

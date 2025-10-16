package com.dahuaboke.mpda.bot.rag.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/9/15 16:43
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DocumentQueryServiceTest {

    @Autowired
    BrProductMapper brProductMapper;


    @Autowired
    private ProductReportQueryService documentQueryService;

    @Test
    public void testProcessProduct() {
    }

    @Test
    public void testMapper () {
        //1. 查询br_product ，获取未处理文件
        QueryWrapper<BrProduct> queryWrapper = new QueryWrapper<BrProduct>()
                .eq("deal_flag", "0");
        List<BrProduct> brProducts = brProductMapper.selectList(queryWrapper);
    }
}

package com.dahuaboke.mpda.bot.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.bot.rag.handler.DbHandler;
import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.scheduler.MarketRankTask;
import com.dahuaboke.mpda.bot.scheduler.RagSearchTask;
import com.dahuaboke.mpda.bot.scheduler.ResetTimeOutTask;
import com.dahuaboke.mpda.bot.tools.ContentManageTool;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageResponse;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class ReserveController {

    @Autowired
    private ContentManageTool contentManageTool;

    @Autowired
    private MarketRankTask marketRankTask;

    @Autowired
    private RagSearchTask ragSearchTask;

    @Autowired
    private ResetTimeOutTask resetTimeOutTask;

    @Autowired
    private VectorStoreRequestHandle requestHandle;

    @Autowired
    private ProductReportQueryService productReportQueryService;

    @Autowired
    private BrProductMapper brProductMapper;

    @GetMapping("/contentManage/downloadFiles/{inmngPlatfFileIndexNo}/{pageCode}")
    public ContentManageResponse downloadFiles(@PathVariable("inmngPlatfFileIndexNo") String inmngPlatfFileIndexNo, @PathVariable("pageCode") String pageCode) {
        return contentManageTool.downloadFilesTool(inmngPlatfFileIndexNo, pageCode);
    }


    @GetMapping("model/sendC014005")
    public List sendC014005() {
        HashMap<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("file_name_keywords", "000927");
        conditionMap.put("file_type", "1");
        C014005Resp c014005Resp = requestHandle.sendC014005(FundConstant.INDEX_NAME, conditionMap, Map.of());
        List resultMap = c014005Resp.getResultList();
        return resultMap;
    }


    @GetMapping("model/queryReport")
    public BrProductReport queryReport() {
        BrProduct brProduct = new BrProduct();
        brProduct.setFundCode("008256");
        brProduct.setFundProdtFullNm("南方中债1-5年国开行债券指数A");
        BrProductReport report = productReportQueryService.queryFundProduct(brProduct);
        return report;
    }

    @GetMapping("task/marketRankTask")
    public void marketRankTask() {
        marketRankTask.marketRankJob();
    }

    @GetMapping("task/ragSearchTask")
    public void ragSearchTask() {
        ragSearchTask.ragSearchJob();
    }

    @GetMapping("task/resetTimeOutTask")
    public void resetTimeOutTask() {
        resetTimeOutTask.ragSearchJob();
    }


    @PostMapping("fund/processDataList")
    public void processDataList(@RequestBody List<String> fundCodes) {
        ArrayList<BrProduct> brProducts = new ArrayList<>();
        for (String fundCode : fundCodes) {
            LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                    .eq(BrProduct::getFundCode, fundCode);
            List<BrProduct> products = brProductMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(products)) {
                brProducts.addAll(products);
            }
        }
        ragSearchTask.processDataList(brProducts);
    }


    @PostMapping("file/downLoadFile")
    public void downLoadFile(@RequestBody List<String> fundCodes) {
        for (String fundCode : fundCodes) {
            LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                    .eq(BrProduct::getFundCode, fundCode);
            List<BrProduct> products = brProductMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(products)) {
                BrProduct brProduct = products.get(0);
                ContentManageResponse contentManageResponse = contentManageTool.downloadFilesTool(brProduct.getInmngPlatfFileIndexNo(), brProduct.getPageCode());
                if (!contentManageResponse.isDownloadSuccess()) {
                    throw new MpdaRuntimeException("内管文件下载失败");
                }
            }
        }
    }

}

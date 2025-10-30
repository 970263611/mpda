package com.dahuaboke.mpda.bot.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.bot.model.common.ResponseCode;
import com.dahuaboke.mpda.bot.rag.handler.DbHandler;
import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.scenes.entity.PlatformRep;
import com.dahuaboke.mpda.bot.scheduler.MarketRankTask;
import com.dahuaboke.mpda.bot.scheduler.MonitorTask;
import com.dahuaboke.mpda.bot.scheduler.RagSearchTask;
import com.dahuaboke.mpda.bot.scheduler.ResetTimeOutTask;
import com.dahuaboke.mpda.bot.tools.ContentManageTool;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageResponse;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankReq;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.entity.resp.C014011Resp;
import com.dahuaboke.mpda.client.handle.RerankModelRequestHandle;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private MonitorTask monitorTask;

    @Autowired
    RerankModelRequestHandle rerankModelRequestHandle;

    @Autowired
    ChatModel chatModel;

    @Autowired
    DbHandler dbHandler;

    @Autowired
    RobotService robotService;

    /**
     * 内管文件下载
     *
     * @param inmngPlatfFileIndexNo
     * @param pageCode
     * @return
     */
    @GetMapping("/contentManage/downloadFiles/{inmngPlatfFileIndexNo}/{pageCode}")
    public ContentManageResponse downloadFiles(@PathVariable("inmngPlatfFileIndexNo") String inmngPlatfFileIndexNo, @PathVariable("pageCode") String pageCode) {
        return contentManageTool.downloadFilesTool(inmngPlatfFileIndexNo, pageCode);
    }

    @GetMapping("model/chat")
    public String chat() {
        Prompt text = new Prompt("李白是哪一年的");
        String content = chatModel.call(text).getResult().getOutput().getText();
        return content;
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

    @GetMapping("model/sendC014011")
    public List sendC014011() {
        ArrayList<String> texts = new ArrayList<>();
        texts.add("人工智能技术被广泛应用于医疗影像诊断，帮助医生检测癌症等疾病");
        texts.add("机器学习算法在金融领域用于风险评估和欺诈检测");
        texts.add("医疗机器人通过AI技术辅助外科手术，提高手术精度");
        texts.add("人工智能在教育领域的应用包括个性化学习推荐系统");

        C014011Resp c014011Resp = rerankModelRequestHandle.sendC014011("人工智能在医疗领域的应用", texts);
        List<C014011Resp.ProcessData> processData = c014011Resp.getProcessData();

        //取topK个
        return processData.stream()
                .sorted(Comparator.comparingDouble(C014011Resp.ProcessData::getScore).reversed())
                .limit(10)
                .map(data -> texts.get(data.getIndex()))
                .toList();
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

    @GetMapping("sql/updateDealFlag")
    public int updateDealFlag() {
        return dbHandler.updateFailProduct();
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
            for (BrProduct brProduct : products) {
                ContentManageResponse contentManageResponse = contentManageTool.downloadFilesTool(brProduct.getInmngPlatfFileIndexNo(), brProduct.getPageCode());
                if (!contentManageResponse.isDownloadSuccess()) {
                    throw new MpdaRuntimeException("内管文件下载失败");
                }
            }
        }
    }

    /**
     * 监控数据清理
     */
    @GetMapping("task/MonitorTask")
    public void MonitorTask() {
        monitorTask.MonitorDeleteTaskJob();
    }


    /**
     * 创建文件夹
     * @param dirPath
     */
    @PostMapping("createDir")
    public void createDir(@RequestBody String dirPath){
        contentManageTool.createDir(dirPath);
    }

    @GetMapping("dealDldFlnm")
    public void creadealDldFlnmteDir(){
        String s = robotService.dealDldFlnm();
    }

}

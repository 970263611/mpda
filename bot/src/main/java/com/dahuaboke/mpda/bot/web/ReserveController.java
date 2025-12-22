package com.dahuaboke.mpda.bot.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.bot.rag.exception.AdvancedFundCodeExtractor;
import com.dahuaboke.mpda.bot.rag.exception.InitExceptionService;
import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.rag.service.ProductSummaryQueryService;
import com.dahuaboke.mpda.bot.scheduler.AddProductNameTask;
import com.dahuaboke.mpda.bot.scheduler.CalculatedRateBondTask;
import com.dahuaboke.mpda.bot.scheduler.ExceptionRetryTask;
import com.dahuaboke.mpda.bot.scheduler.MarketRankTask;
import com.dahuaboke.mpda.bot.scheduler.MonitorTask;
import com.dahuaboke.mpda.bot.scheduler.RagSearchTask;
import com.dahuaboke.mpda.bot.scheduler.ResetTimeOutTask;
import com.dahuaboke.mpda.bot.scheduler.UpdateProductValidFlagTask;
import com.dahuaboke.mpda.bot.tools.ContentManageTool;
import com.dahuaboke.mpda.bot.tools.dao.BrProductMapper;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageResponse;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankESBDto;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.service.BrPdfParseExceptionsService;
import com.dahuaboke.mpda.bot.tools.service.BrProductService;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.entity.resp.C014011Resp;
import com.dahuaboke.mpda.client.handle.RerankModelRequestHandle;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import com.dahuaboke.mpda.core.client.ChatClientManager;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpHeaders;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
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
    private CalculatedRateBondTask calculatedRateBondTask;

    @Autowired
    private VectorStoreRequestHandle requestHandle;

    @Autowired
    private ProductReportQueryService productReportQueryService;

    @Autowired
    private ProductSummaryQueryService productSummaryQueryService;

    @Autowired
    private BrProductMapper brProductMapper;

    @Autowired
    private MonitorTask monitorTask;

    @Autowired
    RerankModelRequestHandle rerankModelRequestHandle;

    @Autowired
    ChatModel chatModel;

    @Autowired
    ChatClient chatClient;

    @Autowired
    RobotService robotService;

    @Autowired
    BrProductService brProductService;

    @Autowired
    ExceptionRetryTask exceptionRetryTask;

    @Autowired
    private UpdateProductValidFlagTask updateProductValidFlagTask;

    @Autowired
    private AddProductNameTask addProductNameTask;

    @Autowired
    private InitExceptionService initExceptionService;

    @Autowired
    private AdvancedFundCodeExtractor advancedFundCodeExtractor;

    @Autowired
    private BrPdfParseExceptionsService brPdfParseExceptionsService;


    /* ============================================定时任务=========================================================== */
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

    @GetMapping("task/monitorTask")
    public void MonitorTask() {
        monitorTask.MonitorDeleteTaskJob();
    }

    @GetMapping("task/productRecommend")
    public void productRecommend() {
        updateProductValidFlagTask.productRecommend();
    }

    @PostMapping("task/calRateBondTask")
    public void calRateBond(@RequestBody List<String> fundCodes) {
        if (CollectionUtils.isEmpty(fundCodes)) {
            calculatedRateBondTask.calculatedRateBondJob();
        } else {
            calculatedRateBondTask.calculatedRateBondJob(fundCodes);
        }
    }
    @GetMapping("task/addProductNameTask")
    public void addProductNameTask() {
        addProductNameTask.addProductNameJob();
    }

    @GetMapping("task/exceptionRetryTask")
    public void exceptionRetryTask() {
        exceptionRetryTask.exceptionRetryTask();
    }

    /* ============================================模型接口=========================================================== */
    @GetMapping("model/chat")
    public String chat() {
        ChatClient.CallResponseSpec call = chatClient.prompt("李白是哪一年的").call();
        return call.content();
    }

    @GetMapping("model/model")
    public String model() {
        return chatModel.call("李白是哪一年的");
    }


    /* ============================================新核心接口=========================================================== */
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

    /* ============================================内管接口=========================================================== */
    @GetMapping("/contentManage/downloadFiles/{inmngPlatfFileIndexNo}/{pageCode}")
    public ContentManageResponse downloadFiles(@PathVariable("inmngPlatfFileIndexNo") String inmngPlatfFileIndexNo, @PathVariable("pageCode") String pageCode) {
        return contentManageTool.downloadFilesTool(inmngPlatfFileIndexNo, pageCode);
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

    @PostMapping("createDir")
    public void createDir(@RequestBody String dirPath) {
        contentManageTool.createDir(dirPath);
    }

    /* ============================================平台=========================================================== */

    /**
     * 通过债基类型和时间查询市场报告
     * ESB下载入口
     *
     * @param finBondType
     * @param period
     */
    @GetMapping("/selectMarketReportByTimeAndFundType/{finBondType}/{period}")
    public String selectMarketReportByTimeAndFundType(@PathVariable("finBondType") String finBondType, @PathVariable("period") String period) {
        List<MarketRankESBDto> marketRankESBDtoList = robotService.selectMarketReportByTimeAndFundType(finBondType, period);
        Gson gson = new Gson();
        return gson.toJson(marketRankESBDtoList);
    }

    /* ============================================sql更新=========================================================== */
    @GetMapping("sql/updateDealFlag")
    public int updateProcessingProduct(@RequestParam("dealFlag") String dealFlag) {
        return brProductService.updateProcessingProduct(dealFlag);
    }


    @GetMapping("sql/updateExceptionCount")
    public int updateExceptionCount() {
        return brPdfParseExceptionsService.updateCount();
    }


    /* ============================================文件处理===================================================== */
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

    @GetMapping("fund/queryReport")
    public String queryReport(@RequestParam("fundCode")String fundCode,@RequestParam("name")String name, @RequestParam("question")String question, @RequestParam("enableMonitor")boolean enableMonitor) {

        return productReportQueryService.monitorSingleQuestion(fundCode,name,question,enableMonitor);
    }

    @GetMapping("fund/querySummary")
    public String querySummary(@RequestParam("fundCode")String fundCode,@RequestParam("name")String name, @RequestParam("question")String question, @RequestParam("enableMonitor")boolean enableMonitor) {

        return productSummaryQueryService.monitorSingleQuestion(fundCode,name,question,enableMonitor);
    }



    @GetMapping("fund/init")
    public void initException(){
        initExceptionService.initEmpty();
    }

    @GetMapping("fund/extraErrorLog")
    public void extraErrorLog(@RequestParam("path") String path, @RequestParam("startDay") int startDay ){
        advancedFundCodeExtractor.extraErrorLog(path,startDay);
    }



    /**
     * 计算
     *
     * @param localDate 2020-10-10
     * @return
     */
    @GetMapping("dealDldFlnm/{localDate}")
    public String creadealDldFlnmteDir(@PathVariable("localDate") String localDate) {
        LocalDate parse = LocalDate.parse(localDate);
        return robotService.dealDldFlnm(parse);
    }


    /* ============================================其余处理=========================================================== */
    @GetMapping("updateModel")
    public void updateModel(@RequestParam("modelName") String modelName, @RequestParam("key") String key) {
        ChatClientManager chatClientManager = SpringUtil.getBean(ChatClientManager.class);
        DefaultChatClient chatClient = (DefaultChatClient) SpringUtil.getBean(ChatClient.class);
        OpenAiChatModel model = (OpenAiChatModel) SpringUtil.getBean(ChatModel.class);
        OpenAiChatOptions defaultOptions = (OpenAiChatOptions) model.getDefaultOptions();
        defaultOptions.setModel(modelName);
        Map<String, String> httpHeaders = defaultOptions.getHttpHeaders();
        httpHeaders.put(HttpHeaders.AUTHORIZATION, key);

        reflect(model, defaultOptions);
        reflect(chatClient, defaultOptions);
        reflect(chatClientManager.getChatClient(), defaultOptions);
    }

    private void reflect(ChatModel chatModel, OpenAiChatOptions defaultOptions) {
        try {
            Class<?> modelClass = chatModel.getClass();
            Field requestField = modelClass.getDeclaredField("defaultOptions");
            requestField.setAccessible(true);
            requestField.set(chatModel, defaultOptions);
        } catch (Exception e) {
            throw new MpdaRuntimeException(e);
        }
    }

    private void reflect(ChatClient chatClient, OpenAiChatOptions defaultOptions) {
        try {
            Class<?> chatClientClass = chatClient.getClass();
            Field requestField = chatClientClass.getDeclaredField("defaultChatClientRequest");
            requestField.setAccessible(true);

            DefaultChatClient.DefaultChatClientRequestSpec requestSpec = (DefaultChatClient.DefaultChatClientRequestSpec) requestField.get(chatClient);
            Class<?> requestSpecClass = requestSpec.getClass();
            Field options = requestSpecClass.getDeclaredField("chatOptions");
            options.setAccessible(true);
            OpenAiChatOptions newOptions = (OpenAiChatOptions) options.get(requestSpec);
            newOptions.setHttpHeaders(defaultOptions.getHttpHeaders());
            newOptions.setModel(defaultOptions.getModel());
            options.set(requestSpec, newOptions);
        } catch (Exception e) {
            throw new MpdaRuntimeException(e);
        }
    }

}

package com.dahuaboke.mpda.bot.rag.exception;

/**
 * @Desc: 提取存量异常日志的代码，应该只会使用一次
 * @Author：zhh
 * @Date：2025/11/21 17:22
 */
import com.dahuaboke.mpda.bot.tools.entity.BrPdfParseExceptions;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import com.dahuaboke.mpda.bot.tools.enums.FundInfoType;
import com.dahuaboke.mpda.bot.tools.enums.PdfExceptionType;
import com.dahuaboke.mpda.bot.tools.service.BrPdfParseExceptionsService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvancedFundCodeExtractor {

    private static final Logger log = LoggerFactory.getLogger(AdvancedFundCodeExtractor.class);


    private static final Pattern FUND_CODE_PATTERN = Pattern.compile("\\b\\d{6}\\b");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    BrPdfParseExceptionsService brPdfParseExceptionsService;

    public void extraErrorLog(String path, int startDay){
        log.info("开始提取异常日志.....");
        String logFilePattern = "bot-error.%s.%d.log"; // 日志文件命名模式

        try {
            FundCodeExtractionResult result = extractFundCodes(path, startDay, logFilePattern);
            List<String> fundCodes = result.getFundCodes();
            if(CollectionUtils.isNotEmpty(fundCodes)){
                fundCodes.forEach(this::insertExceptionTable);
            }
            log.info("处理完成!");
            log.info("成功处理的文件数: " + result.getProcessedFiles());
            log.info("提取到的唯一基金代码数: " + result.getFundCodes().size());
        } catch (Exception e) {
            log.info("处理失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 提取基金代码的主要方法
     */
    public static FundCodeExtractionResult extractFundCodes(String directoryPath,
                                                            int startDay,
                                                            String filePattern) {
        Set<String> fundCodeSet = new HashSet<>();
        int processedFiles = 0;
        LocalDate currentDate = LocalDate.now();

        // 计算开始日期（当前月的指定日期）
        LocalDate startDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), startDay);

        // 如果指定的开始日期在当前日期之后，则使用上个月的日期
        if (startDate.isAfter(currentDate)) {
            startDate = startDate.minusMonths(1);
        }

        log.info("开始日期: " + startDate);
        log.info("结束日期: " + currentDate);

        // 遍历日期范围
        for (LocalDate date = startDate; !date.isAfter(currentDate); date = date.plusDays(1)) {
            // 处理每天的日志文件
            for (int fileIndex = 0; ; fileIndex++) {
                String fileName = String.format(filePattern, date.format(DATE_FORMATTER), fileIndex);
                File logFile = new File(directoryPath, fileName);

                if (!logFile.exists()) {
                    break;
                }

                if (logFile.isFile() && logFile.canRead()) {
                    log.info("处理文件: " + fileName);
                    int codesFound = extractFromSingleFile(logFile, fundCodeSet);
                    processedFiles++;
                    log.info("  找到 " + codesFound + " 个基金代码");
                }
            }
        }

        return new FundCodeExtractionResult(new ArrayList<>(fundCodeSet), processedFiles);
    }

    /**
     * 从单个文件提取基金代码
     */
    private static int extractFromSingleFile(File file, Set<String> fundCodeSet) {
        int codesInFile = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                codesInFile += extractFromLine(line, fundCodeSet);
            }
        } catch (IOException e) {
            log.info("读取文件失败: " + file.getName() + " - " + e.getMessage());
        }
        return codesInFile;
    }

    /**
     * 从单行文本提取基金代码
     */
    private static int extractFromLine(String line, Set<String> fundCodeSet) {
        int codesInLine = 0;
        Matcher matcher = FUND_CODE_PATTERN.matcher(line);
        while (matcher.find()) {
            String fundCode = matcher.group();
            if (fundCodeSet.add(fundCode)) {
                codesInLine++;
            }
        }
        return codesInLine;
    }

    private void insertExceptionTable(String fundCode){
        try {
            BrPdfParseExceptions reportExceptions = new BrPdfParseExceptions();
            reportExceptions.setFundCode(fundCode);
            reportExceptions.setAncmTpBclsCd(FundInfoType.REPORT.getCode());
            reportExceptions.setExceptionType(PdfExceptionType.FILE_LEVEL.getCode());
            reportExceptions.setCount(0);
            reportExceptions.setStatus(FileDealFlag.PROCESS_FAIL.getCode());
            brPdfParseExceptionsService.insertParseException(reportExceptions);

            BrPdfParseExceptions summaryExceptions = new BrPdfParseExceptions();
            summaryExceptions.setFundCode(fundCode);
            summaryExceptions.setAncmTpBclsCd(FundInfoType.SUMMARY.getCode());
            summaryExceptions.setExceptionType(PdfExceptionType.FILE_LEVEL.getCode());
            summaryExceptions.setCount(0);
            summaryExceptions.setStatus(FileDealFlag.PROCESS_FAIL.getCode());
            brPdfParseExceptionsService.insertParseException(summaryExceptions);
        } catch (Exception e) {
            log.error("提取异常日志时，{}插入异常表失败",fundCode);
        }
    }

    /**
     * 结果封装类
     */
    public static class FundCodeExtractionResult {
        private final List<String> fundCodes;
        private final int processedFiles;

        public FundCodeExtractionResult(List<String> fundCodes, int processedFiles) {
            this.fundCodes = Collections.unmodifiableList(fundCodes);
            this.processedFiles = processedFiles;
        }

        public List<String> getFundCodes() {
            return fundCodes;
        }

        public int getProcessedFiles() {
            return processedFiles;
        }
    }
}

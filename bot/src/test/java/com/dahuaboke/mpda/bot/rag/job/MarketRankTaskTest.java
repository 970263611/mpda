package com.dahuaboke.mpda.bot.rag.job;


import com.dahuaboke.mpda.bot.rag.handler.DbHandler;
import com.dahuaboke.mpda.bot.scheduler.MarketRankTask;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MarketRankTaskTest {

    @Autowired
    MarketRankTask marketRankTask;
    @Autowired
    DbHandler dbHandler;

    @Test
    public void testInsertMarket(){
        for (int i = 0; i < 100; i++) {
            BrMarketProductReport brMarketProductReport = new BrMarketProductReport();
            brMarketProductReport.setFundCode(202308 + i + "");
            brMarketProductReport.setFinBondType((new Random().nextInt(6 ) + 1) + "");
            dbHandler.insertMarketProductReport(brMarketProductReport);
        }
    }

    @Test
    public void testMarketRankJob(){
        marketRankTask.marketRankJob();
    }
}

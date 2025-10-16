package com.dahuaboke.mpda.bot.rag.job;

import com.dahuaboke.mpda.bot.rag.handler.DbHandler;
import com.dahuaboke.mpda.bot.scheduler.RagSearchTask;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RagSearchJobTest {

    @Autowired
    RagSearchTask ragSearchJob;

    @Autowired
    DbHandler dbHandler;
    @Test
    public void test(){
        ragSearchJob.ragSearchJob();
    }

    @Test
    public void testTime(){
        dbHandler.resetTimeout();
    }

    @Test
    public void testMarkAndSelectUnprocessed(){
        List<BrProduct> brProducts = dbHandler.markAndSelectUnprocessed(200);
        System.out.println(brProducts);
    }


    @Test
    public void testDelFile(){

    }

}

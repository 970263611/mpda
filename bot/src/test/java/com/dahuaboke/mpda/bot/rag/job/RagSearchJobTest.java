package com.dahuaboke.mpda.bot.rag.job;

import com.dahuaboke.mpda.bot.scheduler.RagSearchTask;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.service.BrProductService;
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
    BrProductService brProductService;
    @Test
    public void test(){
        ragSearchJob.ragSearchJob();
    }

    @Test
    public void testTime(){
        brProductService.resetTimeout();
    }

    @Test
    public void testMarkAndSelectUnprocessed(){
        List<BrProduct> brProducts = brProductService.markAndSelectUnprocessed(200);
        System.out.println(brProducts);
    }


    @Test
    public void testDelFile(){

    }

}

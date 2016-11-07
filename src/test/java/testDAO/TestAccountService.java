package testDAO;

import com.av.domain.Transaction;
import com.av.services.AccountServceIface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.formular.ACR0531722;
import test.formular.TS1S1722ITEM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by alexey on 31.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath:infrastructure.xml"})
public class TestAccountService {

@Autowired
   private AccountServceIface accountServceIface;



    private ACR0531722 genDoc() {

        ACR0531722 rr = new ACR0531722();
        rr.setFUND_SOURCE_CODE("1");

        for (int i = 0; i <  9 ; i++) {
            TS1S1722ITEM line = new TS1S1722ITEM();
            line.setBCC("10000000000000000021" + new Random().nextInt(9));
            if (i%3 == 0)   line.setSection("кан");
            if (i%3 == 1)   line.setSection("аю");
            if (i%3 == 2)   line.setSection("онтп");
            rr.getFS_S1_S1_722_LIST().add(line);
        }




        return  rr;
    }


    @Test
    public void testService() {

        ACR0531722 doc = genDoc();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy hh.mm.ss SSS");
        List<Transaction>  tr  = new ArrayList<Transaction>();
     for (int i =0 ; i < 10 ; i++) {
         System.out.println(df.format(new Date()));
           tr = accountServceIface.genTransaction(doc , "CODE_1");
         System.out.println(df.format(new Date()));
     }




        for (Transaction t : tr) {
            System.out.println(t);
        }


    }

}

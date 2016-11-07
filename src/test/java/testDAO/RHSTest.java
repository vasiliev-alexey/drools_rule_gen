package testDAO;

import com.av.dao.EventDAO;
import com.av.domain.Document;
import com.av.domain.Event;
import com.av.domain.EventSetting;
import com.av.utils.RuleGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by alexey on 28.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath:infrastructure.xml"})
public class RHSTest {

@Autowired
    private EventDAO eventDAO;

@Autowired
    private RuleGenerator ruleGenerator;

    @Test
    public  void  t1 () {

        List<EventSetting> list = eventDAO.findEventSetList(1l);
        Document doc = eventDAO.findByID(1L).getDoc();
        for (EventSetting es : list) {

            if (es.getId() == 3) {
            String rhs =    ruleGenerator.getRHS(es, doc);
                System.out.println(rhs);
            }

        }






    }

}

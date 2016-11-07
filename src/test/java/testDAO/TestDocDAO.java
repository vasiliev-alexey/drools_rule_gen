package testDAO;

import com.av.dao.DocumentDAO;
import com.av.domain.Document;
import com.av.domain.DocumentAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by alexey on 17.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath:infrastructure.xml"})
public class TestDocDAO {

    @Autowired
    DocumentDAO documentDAO;

    @Test
    public  void testFindAll() {
        List<Document>  docList     ;
        docList = documentDAO.findAll();

        for (Document d: docList) {
            System.out.println(d);
        }


        System.out.println("--------------");

        Document dd = documentDAO.findById(1L);
        for ( DocumentAttribute da :dd.getDocumentAttributeList()) {
            System.out.println(da);
        }


    }

}

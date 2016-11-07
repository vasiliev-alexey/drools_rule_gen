package testDAO;

import com.av.dao.ConditionDao;
import com.av.dao.DocumentStructDAO;
import com.av.dao.GroupRuleDAO;
import com.av.domain.*;
import com.av.utils.RuleGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by vasil on 28.10.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath:infrastructure.xml"})
public class ConditionTest {

    @Autowired
    private DocumentStructDAO documentStructDAO;
    @Autowired
    private RuleGenerator ruleGenerator;

    @Autowired
    private GroupRuleDAO groupRuleDAO;
    @Autowired
    private ConditionDao conditionDAO;

    //@Test
    public void genConditionRule() {


        HashMap<String, DocumentStructure> ds = documentStructDAO.getDocMetaData(1L);

        Condition cond = conditionDAO.findById(6);

        String condStr = (ruleGenerator.getLHS(cond, ds));
        condStr = condStr.replace("$l.", "line.");
        condStr = condStr.replace("$h.", "header.");
        System.out.println(condStr);


    }


    @Test
    public void genGroupRule() {


        GroupRule r = groupRuleDAO.findGroupRuleById(24);
        HashMap<String, DocumentStructure> ds = documentStructDAO.getDocMetaData(r.getDoc().getId());
        System.out.println(r.getDoc());

        put(r.toString());
        put("-----------------------");

        Collections.sort(r.getItems());

        String headerClass = "";

        String lineClass = "";

        for (String h : ds.keySet()) {

            if (h.equals("HEADER")) headerClass = ds.get(h).getClassCode();
            if (h.equals("LINE")) lineClass = ds.get(h).getClassCode();
        }


        boolean hasnext = false;


        for (GroupRuleItem item : r.getItems()) {

            String condStr = (ruleGenerator.getLHS(item.getCondition(), ds));
            condStr = condStr.replace("$l.", "line.");
            condStr = condStr.replace("$h.", "header.");


            if (hasnext) put("else");

            put( " if ( " + condStr + ") {");
            StringBuffer buf = new StringBuffer();

            if (item.getValue()instanceof DocumentAttribute) {
                DocumentAttribute da = (DocumentAttribute) item.getValue();

                if ("LINE".equals(da.getPath())) {
                    buf.append(  "line." + da.getFieldCode());
                } else {
                    buf.append("header." + da.getFieldCode());
                }
            } else if (item.getValue() instanceof ConstantValue) {
                ConstantValue cv = (ConstantValue) item.getValue();
                buf.append("\"" + cv.getCode() + "\"");
            }else if ( item.getValue() instanceof  CustomRule) {
                // ��������� ������� API
                // System.out.println( "|"+ line.getLeftValue() + "|");
                CustomRule cr =  (CustomRule) item.getValue();
                String spec = "";
                String API = cr.getAPI();
                Collections.sort(cr.getCustomRuleParamList());
                for (CustomRuleParam p :cr.getCustomRuleParamList()) {

                    if (p.getAttr() != null) {

                        if (p.getAttr().getPath().equals("LINE")) {
                            spec = spec +  "line."+   p.getAttr().getFieldCode();
                        } else {
                            spec = spec +  "header." +  p.getAttr().getFieldCode();
                        }
                    } else                     if (p.getNumParam().intValue() > -1) {
                        spec = spec +  " , " +p.getNumParam();
                    }

                }
                // System.out.println("spec =" + spec);
                buf.append(API + "(" + spec + ")");



            }


            put("retVal = " + buf.toString());


            put("}");
            hasnext = true;
        }
        put("}");


    }


    public static void put(String s) {
        System.out.println(s);
    }

}

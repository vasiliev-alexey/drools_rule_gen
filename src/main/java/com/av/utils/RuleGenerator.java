package com.av.utils;

import com.av.dao.DocumentStructDAO;
import com.av.dao.EventDAO;
import com.av.domain.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by vasil on 26.10.2015.
 */
@Service
public class RuleGenerator {

    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private DocumentStructDAO documentStructDAO;





    public void genFileByEventId(long eventId) {


        System.out.println("//**************************************");


        Event e =
                eventDAO.findByID(eventId);


        Document doc = e.getDoc();
        HashMap<String, DocumentStructure> ds = documentStructDAO.getDocMetaData(doc.getId());
        String packName = ds.get("HEADER").getPackageName();
        put("import " + packName + ".*;");
        put("import com.av.utils.CustomSource;");
        put("global CustomSource customSource;");
        put("import  com.av.domain.Transaction;");
        put("dialect  \"mvel\"");


        for (EventSetting es : e.getEventSettingList()) {
            genRuleEventSetting(e , es, doc, ds);
        }

        System.out.println("//**************************************");
    }


    private void genRuleEventSetting(Event e , EventSetting es, Document doc, HashMap<String, DocumentStructure> ds) {

        String headerClassName;
        String lineClassName;
        String lineCollectionClassName;
        String packName;
        //  System.out.println(es);

        getLHS(es.getCondition(), ds);

        headerClassName = ds.get("HEADER").getClassCode();
        lineClassName = ds.get("LINE").getClassCode();
        lineCollectionClassName = ds.get("LINE").getNode();


        put("rule " + "\"" + es.getCode() + "\"");
        put("agenda-group \"" +  e.getCode() + "\"");
        put(" when ");
        // получить класс заголовка
        put(" $h :" + headerClassName + "()");
        put("$l : " + lineClassName +
                "  (" + getLHS(es.getCondition(), ds) + " ) from $h." + lineCollectionClassName + "");
        // получить класс строк

        put("then");
        put("//System.out.println(\"" + es.getCode() + "\" );");

        put(getRHS(es , doc));
        put("//System.out.println( tr );");
        put("insert(tr);");

        put("end");


    }


    private void put(String str) {
        System.out.println(str);
    }


    public String getLHS(Condition condition, HashMap<String, DocumentStructure> ds) {

        String condString = "";

        StringBuffer buf = new StringBuffer();


        Collections.sort(condition.getConditionLineList());

        for (ConditionLine line : condition.getConditionLineList()) {
            // System.out.println(line);

            if (line.getLeftBracket() != null) buf.append(line.getLeftBracket());

            if (line.getLeftValue() instanceof DocumentAttribute) {
                DocumentAttribute da = (DocumentAttribute) line.getLeftValue();
                // надо определить принадлежит он заголовку или строке
                if ("LINE".equals(da.getPath())) {
                    buf.append("$l." + da.getFieldCode());
                } else {
                    buf.append("$h." + da.getFieldCode());
                }


            } else if (line.getLeftValue() instanceof CustomRule) {
                // System.out.println( "|"+ line.getLeftValue() + "|");
                CustomRule cr = (CustomRule) line.getLeftValue();
                String spec = "";
                String API = "customSource." + cr.getAPI();
                Collections.sort(cr.getCustomRuleParamList());
                for (CustomRuleParam p : cr.getCustomRuleParamList()) {

                    if (p.getAttr() != null) {

                        if (p.getAttr().getPath().equals("LINE")) {
                            spec = spec + "$l." + p.getAttr().getFieldCode();
                        } else {
                            spec = spec + "$h." + p.getAttr().getFieldCode();
                        }
                    } else if (p.getNumParam().intValue() > -1) {
                        spec = spec + " , " + p.getNumParam();
                    }

                }
                // System.out.println("spec =" + spec);
                buf.append(API + "(" + spec + ")");


            }
            // добавляем выражение
            if (line.getExpression() != null) {
                buf.append(convertExpression(line.getExpression()));

            }
            if (line.getRightValue() instanceof DocumentAttribute) {
                DocumentAttribute da = (DocumentAttribute) line.getRightValue();
                // надо определить принадлежит он заголовку или строке
                if ("LINE".equals(da.getPath())) {
                    buf.append("$l." + da.getFieldCode());
                } else {
                    buf.append("$h." + da.getFieldCode());
                }
            } else if (line.getRightValue() instanceof ConstantValue) {
                ConstantValue cv = (ConstantValue) line.getRightValue();
                buf.append("\"" + cv.getCode() + "\"");
            } else if (line.getRightValue() instanceof CustomRule) {
                // обработка функций API
                // System.out.println( "|"+ line.getLeftValue() + "|");
                CustomRule cr = (CustomRule) line.getRightValue();
                String spec = "";
                String API = cr.getAPI();
                Collections.sort(cr.getCustomRuleParamList());
                for (CustomRuleParam p : cr.getCustomRuleParamList()) {

                    if (p.getAttr() != null) {

                        if (p.getAttr().getPath().equals("LINE")) {
                            spec = spec + "$l." + p.getAttr().getFieldCode();
                        } else {
                            spec = spec + "$h." + p.getAttr().getFieldCode();
                        }
                    } else if (p.getNumParam().intValue() > -1) {
                        spec = spec + " , " + p.getNumParam();
                    }

                }
                // System.out.println("spec =" + spec);
                buf.append(API + "(" + spec + ")");


            }
            if (line.getRightBracket() != null) buf.append(line.getRightBracket());

            if (line.getOperator() != null) {
                buf.append(" ");
                buf.append(convertOperator(line.getOperator()));
            }


            // System.out.println(buf.toString());
            //  buf.delete(0, buf.length());

        }

        return buf.toString();
    }


    public String getRHS(EventSetting es, Document doc) {
        HashMap<String, DocumentStructure> ds = documentStructDAO.getDocMetaData(doc.getId());

        StringBuffer resBuf = new StringBuffer();

        Collections.sort(es.getSegmentSettingList());
        resBuf.append("Transaction tr = new Transaction();" + "\n");
        HashMap<String, String> hs = new HashMap();

        for (SegmentSetting ss : es.getSegmentSettingList()) {


            // System.out.println(ss);
            String code = "s" + ss.getTypeEntry() + ss.getSegmentNum();
            if (ss.getValue() instanceof ConstantValue) {
                ConstantValue cs = (ConstantValue) ss.getValue();
                resBuf.append("tr.set" + code + "(\"" + cs.getCode() + "\"); "+ "\n");
            }
            if (ss.getValue() instanceof DocumentAttribute) {
                DocumentAttribute da = (DocumentAttribute) ss.getValue();
                // надо определить принадлежит он заголовку или строке
                if ("LINE".equals(da.getPath())) {
                    // hs.put(code, "$l." + da.getFieldCode());
                    resBuf.append("tr.set" + code + "(" + "$l." + da.getFieldCode() + ");"+ "\n");
                } else {
                    resBuf.append("tr.set" + code + "(" + "$h." + da.getFieldCode() + ");"+ "\n");
                }
            }

            // API

            if (ss.getValue() instanceof CustomRule) {
                // обработка функций API
                // System.out.println( "|"+ line.getLeftValue() + "|");
                CustomRule cr = (CustomRule)ss.getValue();
                String spec = "";
                String API = "customSource." +cr.getAPI();
                Collections.sort(cr.getCustomRuleParamList());
                for (CustomRuleParam p : cr.getCustomRuleParamList()) {

                    if (p.getAttr() != null) {

                        if (p.getAttr().getPath().equals("LINE")) {
                            spec = spec + "$l." + p.getAttr().getFieldCode();
                        } else {
                            spec = spec + "$h." + p.getAttr().getFieldCode();
                        }
                    } else if (p.getNumParam().intValue() > -1) {
                        spec = spec + " , " + p.getNumParam();
                    }

                }
                // System.out.println("spec =" + spec);
               // buf.append(API + "(" + spec + ")");
                resBuf.append("tr.set" + code + "(" + API + "(" + spec + ")"+ ");"+ "\n");

            }


            if (ss.getValue() instanceof GroupRule) {
                GroupRule r = (GroupRule) ss.getValue();
                boolean hasnext = r.getItems().size() == 1;
                int total = r.getItems().size();
                int i = 0;

                for (GroupRuleItem item : r.getItems()) {

                    String condStr = (getLHS(item.getCondition(), ds));

                    i++;


                    if (i > 1) {
                        resBuf.append(" } else if ( " + condStr + ") {"+ "\n");
                    } else {
                        resBuf.append(" if ( " + condStr + ") {"+ "\n");
                    }
                    StringBuffer buf = new StringBuffer();
                  //  buf.append("\n");
                    if (item.getValue() instanceof DocumentAttribute) {
                        DocumentAttribute da = (DocumentAttribute) item.getValue();

                        if ("LINE".equals(da.getPath())) {
                            buf.append("$l." + da.getFieldCode());
                        } else {
                            buf.append("$h." + da.getFieldCode());
                        }
                    } else if (item.getValue() instanceof ConstantValue) {
                        ConstantValue cv = (ConstantValue) item.getValue();
                        buf.append("\"" + cv.getCode() + "\"");
                    } else if (item.getValue() instanceof CustomRule) {
                        // ????????? ??????? API
                        // System.out.println( "|"+ line.getLeftValue() + "|");
                        CustomRule cr = (CustomRule) item.getValue();
                        String spec = "";
                        String API = "customSource." + cr.getAPI();
                        Collections.sort(cr.getCustomRuleParamList());
                        for (CustomRuleParam p : cr.getCustomRuleParamList()) {

                            if (p.getAttr() != null) {

                                if (p.getAttr().getPath().equals("LINE")) {
                                    spec = spec + "$l." + p.getAttr().getFieldCode();
                                } else {
                                    spec = spec + "$h." + p.getAttr().getFieldCode();
                                }
                            } else if (p.getNumParam().intValue() > -1) {
                                spec = spec + " , " + p.getNumParam();
                            }

                        }
                        // System.out.println("spec =" + spec);
                        buf.append(API + "(" + spec + ")");


                    }

                    resBuf.append("tr.set" + code + "(" + buf.toString() + ");" + "\n");
                    // put("retVal = " + buf.toString());

                    if (i == total) resBuf.append("}"+ "\n");


                    hasnext = true;
                }
            }


            //System.out.println(hs);
        }
        return  resBuf.toString();
    }


    private static String convertExpression(String exp) {
        String retval = "";
        if (exp.equals("Равно")) {
            retval = " == ";
        }
        if (exp.equals("Не пусто")) {
            retval = " != null ";
        }
        if (exp.equals("Пусто")) {
            retval = " == null ";
        }
        if (exp.equals("Не равно")) {
            retval = " != ";
        }

        return retval;
    }

    private static String convertOperator(String oper) {
        String retval = "";
        if (oper.equals("И")) {
            retval = " && ";
        } else {
            retval = " || ";
        }
        return retval;
    }

}

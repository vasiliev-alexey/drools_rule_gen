package com.av.dao;

import com.av.domain.AbstractValue;
import com.av.domain.Condition;
import com.av.domain.ConditionLine;
import com.av.domain.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexey on 19.10.15.
 */
@Repository
public class ConditionDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private AbstractValueDAO abstractValueDAO;
    @Autowired
    private ConstantValueDAO constantValueDAO;

    private static final String FIND_BY_ID =
            "select c.CONDITION_ID , c.CONDITION_CODE , c.NAME ,\n" +
                    "cl.CONDITION_LINE_ID , cl.USER_SEQUENCE , cl.BRACKET_RIGHT_CODE,\n" +
                    "cl.LEFT_OBJECT_ID , cl.EXPRESSION , LOGIC_OPERATOR , \n" +
                    "cl.BRACKET_LEFT_CODE, cl.RIGHT_OBJECT_ID\n" +
                    "from CONDITION c left outer join CONDITION_LINE cl\n" +
                    "on c.CONDITION_ID = cl.CONDITION_ID where c.condition_id = :ID";

    public Condition findById(int id) {

        HashMap<String, Integer> param = new HashMap<String, Integer>();
        param.put("ID", id);


        return template.query(FIND_BY_ID, param, new ConditionResultSetExtractor());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Condition saveCondition(Condition condition) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        HashMap<String, Object> hParam = new HashMap<String, Object>();
        hParam.put("CODE", condition.getCode());
        hParam.put("NAME", condition.getName());
        hParam.put("CONDITION_ID", condition.getId());


        HashMap<String, Object> lParam = new HashMap<String, Object>();

        if (condition.getId() > 0) {
            template.update("update condition  set condition_code = :CODE , name = :NAME " +
                    "where condition_id= :CONDITION_ID", hParam);
        } else {
            template.update("insert into condition (condition_code , name) values (:CODE , :NAME)",
                    new MapSqlParameterSource(hParam), holder, new String[]{"CONDITION_ID"});
            condition.setId(holder.getKey().intValue());

        }

        if (condition.getConditionLineList() != null) {
            template.update("delete from condition_line  where condition_id= :CONDITION_ID", hParam);

            for (ConditionLine line : condition.getConditionLineList()) {


                lParam.put("CONDITION_ID", condition.getId());
                lParam.put("USER_SEQUENCE", line.getUserSeq());
                lParam.put("BRACKET_RIGHT_CODE", line.getRightBracket());
                if (line.getLeftValue() == null) lParam.put("LEFT_OBJECT_ID", null);
                if (line.getLeftValue() != null) lParam.put("LEFT_OBJECT_ID", line.getLeftValue().getId());
                lParam.put("EXPRESSION", line.getExpression());
                lParam.put("LOGIC_OPERATOR", line.getOperator());
                lParam.put("BRACKET_LEFT_CODE", line.getLeftBracket());


                if (line.getRightValue() instanceof ConstantValue && line.getRightValue().getId() == 0
                        && !line.getRightValue().getCode().equals(""))

                {

                    constantValueDAO.saveConst((ConstantValue) line.getRightValue());
                }

                if (line.getRightValue() == null) lParam.put("RIGHT_OBJECT_ID", null);
                if (line.getRightValue() != null) lParam.put("RIGHT_OBJECT_ID", line.getRightValue().getId());


                template.update("insert into CONDITION_LINE \n" +
                                "(CONDITION_ID , USER_SEQUENCE , BRACKET_RIGHT_CODE,\n" +
                                "LEFT_OBJECT_ID ,EXPRESSION , LOGIC_OPERATOR , BRACKET_LEFT_CODE, RIGHT_OBJECT_ID)\n" +
                                "values \n" +
                                "(:CONDITION_ID , :USER_SEQUENCE , :BRACKET_RIGHT_CODE, :LEFT_OBJECT_ID ,:EXPRESSION ,\n" +
                                " :LOGIC_OPERATOR , :BRACKET_LEFT_CODE, :RIGHT_OBJECT_ID)", new MapSqlParameterSource(lParam),
                        holder, new String[]{"CONDITION_LINE_ID"});
                line.setId(holder.getKey().intValue());
            }

        }

        return condition;
    }


    public void deleteConditionByGroupRuleID(long groupRuleID) {

            HashMap<String , Long> p = new HashMap<String, Long>();
        p.put("GROUP_RULE_ID" , groupRuleID);

        template.update("delete from CONDITION_LINE  where condition_id in ( " +
                "select gr.CONDITION_id from   GROUP_RULE_ITEM gr" +
                " where gr.GROUP_RULE_ID = :GROUP_RULE_ID )", p);

        template.update("delete from CONDITION   where condition_id in ( " +
                "select gr.CONDITION_id from   GROUP_RULE_ITEM gr" +
                " where gr.GROUP_RULE_ID = :GROUP_RULE_ID )" , p);

    }

    private class ConditionResultSetExtractor implements ResultSetExtractor<Condition> {
        public Condition extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Condition condition = new Condition();

            while (resultSet.next()) {
                if (resultSet.isFirst()) {
                    condition.setName(resultSet.getString("NAME"));
                    condition.setCode(resultSet.getString("CONDITION_CODE"));
                    condition.setId(resultSet.getLong("CONDITION_ID"));
                    condition.setConditionLineList(new ArrayList<ConditionLine>());
                }

                if (resultSet.getInt("CONDITION_LINE_ID") > 0) {
                    ConditionLine line = new ConditionLine();
                    line.setId(resultSet.getLong("CONDITION_LINE_ID"));
                    line.setUserSeq(resultSet.getInt("USER_SEQUENCE"));
                    line.setLeftBracket(resultSet.getString("BRACKET_LEFT_CODE"));
                    line.setOperator(resultSet.getString("LOGIC_OPERATOR"));
                    line.setExpression(resultSet.getString("EXPRESSION"));
                    line.setRightBracket(resultSet.getString("BRACKET_RIGHT_CODE"));


                    int avId = resultSet.getInt("LEFT_OBJECT_ID");
                    if (avId > 0) {

                        AbstractValue av = abstractValueDAO.findByID(avId);
                        line.setLeftValue(av);

                        //System.out.println("Type=" + av.getClass().getCanonicalName());


                        if ("CONSTANT".equals(av.getType()))
                            line.setLeftObjectType("Константа");
                        else if ("ATTRIBUTE".equals(av.getType())) {
                            line.setLeftObjectType("Атрибут документа");
                        } else if ("SOURCE".equals(av.getType())) {
                            line.setLeftObjectType("Источник");
                        }
                    }

                    int avRightId = resultSet.getInt("RIGHT_OBJECT_ID");
                    if (avRightId > 0) {

                        AbstractValue av = abstractValueDAO.findByID(avRightId);
                        line.setRightValue(av);


                        if ("CONSTANT".equals(av.getType()))
                            line.setRightObjectType("Константа");
                        else if ("ATTRIBUTE".equals(av.getType())) {
                            line.setRightObjectType("Атрибут документа");
                        } else if ("SOURCE".equals(av.getType())) {
                            line.setRightObjectType("Источник");
                        }
                    }


                    condition.getConditionLineList().add(line);
                }


            }


            return condition;
        }
    }


}

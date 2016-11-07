package com.av.dao;

import com.av.domain.Document;
import com.av.domain.GroupRule;
import com.av.domain.GroupRuleItem;
import com.av.utils.ConvertAbstractValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexey on 23.10.15.
 */
@Repository
public class GroupRuleDAO {

    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private DocumentDAO documentDAO;
    @Autowired
    private AbstractValueDAO abstractValueDAO;
    @Autowired
    private ConditionDao conditionDao;

    private static final String FIND_ALL =
            "select av.id , av.code , av.name , av.type ,  gr.doc_id , gr.enabled_flag , " +
                    "gr.segment_num from abstract_value av , group_rule gr where gr.group_rule_id = av.id";

    private static final String FIND_ALL_BY_DOC_ID =
            FIND_ALL + " and gr.doc_id =:ID ";
    private static final String FIND_ALL_ENABLED_BY_DOC_ID =
            FIND_ALL_BY_DOC_ID + " and ENABLED_FLAG='Y' and gr.doc_id =:ID ";

    private static final String FIND_ALL_BY_ID =
            FIND_ALL + " and av.id =:ID ";

    private static final String FIND_ALL_ITEM_BY_ID = "SELECT  step_id , group_rule_id , priority , " +
            "object_id , condition_id  FROM GROUP_RULE_item where group_rule_id = :RULE_GROUP_ID";


    public List<GroupRule> findAllByDocId(long id) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ID", id);

        return template.query(FIND_ALL_BY_DOC_ID, param, new GroupRuleResultSetExtractor());

    }

    public List<GroupRule> findAllEnabledByDocId(long id) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ID", id);

        return template.query(FIND_ALL_ENABLED_BY_DOC_ID, param, new GroupRuleResultSetExtractor());

    }

    public GroupRule findGroupRuleById(long id) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ID", id);

        return template.query(FIND_ALL_BY_ID, param, new GroupRuleResultSetExtractor()).get(0);
    }

    @Transactional
    public void saveGroupRule(GroupRule gr) {


        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("TYPE", "GROUP_RULE");
        param.put("CODE", gr.getCode());
        param.put("NAME", gr.getName());
        param.put("DOC_ID", gr.getDoc().getId());
        param.put("ENABLED_FLAG", gr.isEnabled() ? "Y" : "N");
        param.put("SEGMENT_NUM", gr.getSegNum());

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        if (gr.getId() == 0) {
            template.update("insert into abstract_value (code , name , type) values (:CODE , :NAME , :TYPE)",
                    new MapSqlParameterSource(param), holder, new String[]{"ID"});
            gr.setId(holder.getKey().intValue());
            param.put("GROUP_RULE_ID", gr.getId());
            template.update("insert into GROUP_RULE ( group_rule_id, doc_id  , enabled_flag , segment_num) " +
                    " values  ( :GROUP_RULE_ID , :DOC_ID  , :ENABLED_FLAG , :SEGMENT_NUM)", param);


            param.clear();


        } else {
            param.put("ID", gr.getId());
            template.update("update abstract_value set code= :CODE , name = :NAME , type = :TYPE" +
                    " where id = :ID ", param);

            template.update("update GROUP_RULE set  doc_id = :DOC_ID  , enabled_flag  = :ENABLED_FLAG, " +
                    " segment_num = :SEGMENT_NUM where group_rule_id = :ID", param);

        }


        param.put("GROUP_RULE_ID", gr.getId());
        template.update("delete from GROUP_RULE_ITEM  where GROUP_RULE_ID= :GROUP_RULE_ID", param);

        conditionDao.deleteConditionByGroupRuleID(gr.getId());


        for (GroupRuleItem gri : gr.getItems()) {
            param.put("PRIORITY", gri.getUserSeq());

            if (gri.getValue() != null && gri.getValue().getId() == 0) {
                abstractValueDAO.saveValue(gri.getValue());
            }
            param.put("OBJECT_ID", gri.getValue().getId());


            conditionDao.saveCondition(gri.getCondition());
            param.put("CONDITION_ID", gri.getCondition().getId());

            template.update("insert into GROUP_RULE_ITEM ( group_rule_id, priority  , object_id , condition_id) " +
                            " values  ( :GROUP_RULE_ID , :PRIORITY  , :OBJECT_ID , :CONDITION_ID)",
                    new MapSqlParameterSource(param), holder, new String[]{"STEP_ID"});
            gri.setId(holder.getKey().intValue());

        }

    }


    private List<GroupRuleItem> loadItems(long id) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("RULE_GROUP_ID", id);
        return template.query(FIND_ALL_ITEM_BY_ID, param, new GroupRuleItemExtractor());

    }


    private class GroupRuleResultSetExtractor implements ResultSetExtractor<List<GroupRule>> {
        public List<GroupRule> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            List<GroupRule> list = new ArrayList<GroupRule>();
            HashMap<Integer, GroupRule> map = new HashMap<Integer, GroupRule>();


            while (resultSet.next()) {

                GroupRule gr = new GroupRule();
                gr.setId(resultSet.getInt("ID"));
                gr.setType(resultSet.getString("TYPE"));
                gr.setCode(resultSet.getString("CODE"));
                gr.setName(resultSet.getString("NAME"));
                gr.setEnabled("Y".equals(resultSet.getString("ENABLED_FLAG")));
                gr.setSegNum(resultSet.getInt("SEGMENT_NUM"));

                Document doc = documentDAO.findById(resultSet.getLong("DOC_ID"));
                gr.setDoc(doc);
                gr.setItems(loadItems(gr.getId()));


                list.add(gr);


            }
            return list;

        }
    }


    private class GroupRuleItemExtractor implements ResultSetExtractor<List<GroupRuleItem>> {
        public List<GroupRuleItem> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            List<GroupRuleItem> list = new ArrayList<GroupRuleItem>();

            while (resultSet.next()) {
                GroupRuleItem item = new GroupRuleItem();
                item.setId(resultSet.getInt("STEP_ID"));
                item.setUserSeq(resultSet.getInt("priority"));

                item.setValue(abstractValueDAO.findByID(resultSet.getInt("object_id")));
                item.setValueType(ConvertAbstractValue.converetToCodeName(item.getValue().getClass()));

                item.setCondition(conditionDao.findById(resultSet.getInt("condition_id")));
                list.add(item);
            }


            return list;
        }
    }


}

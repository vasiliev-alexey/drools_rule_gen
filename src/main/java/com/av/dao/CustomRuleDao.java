package com.av.dao;

import com.av.domain.CustomRule;
import com.av.domain.CustomRuleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vasil on 20.10.2015.
 */
@Repository
public class CustomRuleDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private DocumentDAO documentDAO;

    private static final String FIND_ALL_CUSTOM_RULES = "SELECT  cr.rule_id , av.type, av.code as  rule_code , av.name as rule_name , cr.API_CODE ," +
            " CR.return_type, cr.doc_id ,\n" +
            "crp.param_id , crp.param_code  , crp.param_description , crp.attribute_code_id , crp.attribute_num , \n" +
            "crp.attribute_string , crp.attribute_date  , crp.POSITION  FROM custom_rule cr inner join abstract_value av  on cr.rule_id = av.id    \n" +
            " left outer join custom_rule_params crp  on cr.rule_id = crp.rule_id";

    public List<CustomRule> findAllByDoc(long doc_id) {

        String SQL_STRING = FIND_ALL_CUSTOM_RULES + " where cr.doc_id=:DOC_ID";
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("DOC_ID", doc_id);
        return template.query(SQL_STRING, param, new CustomRuleResulSetExtractor());


    }

    ;

    public CustomRule findById(long id) {

        String SQL_STRING = FIND_ALL_CUSTOM_RULES + " where av.id=:ID";
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ID", id);
        List<CustomRule> list = template.query(FIND_ALL_CUSTOM_RULES, param, new CustomRuleResulSetExtractor());

        return list.get(0);


    }

    ;


    private class CustomRuleResulSetExtractor implements ResultSetExtractor<List<CustomRule>> {
        public List<CustomRule> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            List<CustomRule> list = new ArrayList<CustomRule>();
            HashMap<Long, CustomRule> map = new HashMap<Long, CustomRule>();

            CustomRule cr;
            while (resultSet.next()) {

                if (map.containsKey(resultSet.getLong("RULE_ID"))) {
                    cr = map.get(resultSet.getLong("RULE_ID"));

                } else {
                    cr = new CustomRule();
                    cr.setId(resultSet.getLong("RULE_ID"));
                    cr.setCustomRuleParamList(new ArrayList<CustomRuleParam>());
                    map.put(cr.getId(), cr);
                    list.add(cr);
                    cr.setName(resultSet.getString("rule_name"));
                    cr.setType(resultSet.getString("TYPE"));
                    cr.setCode(resultSet.getString("rule_code"));
                    cr.setAPI(resultSet.getString("API_CODE"));
                    cr.setDoc(documentDAO.findById(resultSet.getLong("doc_id")));
                    cr.setReturnType(resultSet.getString("return_type"));
                }



                if (resultSet.getLong("param_id") > 0 ) {

                    CustomRuleParam param = new CustomRuleParam();
                    param.setCode(resultSet.getString("param_code"));
                    param.setName(resultSet.getString("param_description"));
                    param.setId(resultSet.getLong("param_id"));

                    if (!resultSet.wasNull() && resultSet.getLong("attribute_code_id") > 0) {

                        param.setAttr(documentDAO.findAttrById(resultSet.getLong("attribute_code_id")));

                    }


                    //param.setAttr();
                    param.setNumParam(resultSet.getInt("attribute_num"));
                    param.setStringParam(resultSet.getString(("attribute_date")));
                    param.setPosition(resultSet.getInt("POSITION"));
                    cr.getCustomRuleParamList().add(param);
                }


            }


            return list;
        }
    }


}

package com.av.dao;

import com.av.domain.AbstractValue;
import com.av.domain.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by alexey on 20.10.15.
 */
@Repository
public class AbstractValueDAO {

    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private CustomRuleDao customRuleDao;
    @Autowired
    private DocumentDAO documentDAO;
    @Autowired
    private GroupRuleDAO groupRuleDAO;
    @Autowired
    private ConstantValueDAO constantValueDAO;

    public AbstractValue findByID(int id) {


        HashMap<String, Integer> param = new HashMap<String, Integer>();
        param.put("id", id);

        AbstractValue retVal = new AbstractValue();

        AbstractValue value = template.queryForObject("select * from abstract_value where id=:id", param, new RowMapper<AbstractValue>() {
            public AbstractValue mapRow(ResultSet resultSet, int i) throws SQLException {

                AbstractValue val = new AbstractValue();
                val.setId(resultSet.getInt("ID"));
                val.setCode(resultSet.getString("CODE"));
                val.setName(resultSet.getString("NAME"));
                val.setType(resultSet.getString("TYPE"));
                return val;
            }
        });
        if (value.getType().equals("CONSTANT")) {
            retVal = new ConstantValue();
            retVal.setCode(value.getCode());
            retVal.setName(value.getName());
            retVal.setId(value.getId());
            retVal.setType(value.getType());

        } else if (value.getType().equals("SOURCE")) {
            //System.out.println("find SOURCE by id=" + value.getId());
            retVal = customRuleDao.findById(value.getId());
        } else if (value.getType().equals("ATTRIBUTE")) {

            //   System.out.println("find attr by id=" + value.getId());

            retVal = documentDAO.findAttrById(value.getId());
        } else if (value.getType().equals("GROUP_RULE")) {


            retVal = groupRuleDAO.findGroupRuleById(value.getId());
        }


        return retVal;
    }


    public AbstractValue saveValue(AbstractValue value) {

        if (value.getType().equals("CONSTANT")) {
            constantValueDAO.saveConst((ConstantValue) value);

        }
        return value;
    }

}

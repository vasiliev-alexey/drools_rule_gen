package com.av.dao;

import com.av.domain.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Created by vasil on 21.10.2015.
 */
@Repository
public class ConstantValueDAO {

    @Autowired
    private NamedParameterJdbcTemplate template;


    public void saveConst(ConstantValue cv) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("TYPE", "CONSTANT");
        param.put("CODE", cv.getCode());
        param.put("NAME", cv.getCode());

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        template.update("insert into abstract_value (code , name , type) values (:CODE , :NAME , :TYPE)",
                new MapSqlParameterSource(param), holder, new String[]{"ID"});
        cv.setId(holder.getKey().intValue());


    }

}

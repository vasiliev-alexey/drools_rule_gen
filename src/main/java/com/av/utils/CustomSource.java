package com.av.utils;

import com.av.dao.ConditionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alexey on 26.10.15.
 */
@Component
public class CustomSource {

    //@Autowired
   // private ConditionDao conditionDao;

    public  String substr (String bcc , int start , int length) {
     //   conditionDao.findById(2)
        return  bcc.substring(start, start + length);
    }

}

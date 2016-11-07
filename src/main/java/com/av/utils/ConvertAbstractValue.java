package com.av.utils;

import com.av.domain.ConstantValue;
import com.av.domain.CustomRule;
import com.av.domain.DocumentAttribute;
import com.av.domain.GroupRule;

/**
 * Created by alexey on 24.10.15.
 */
public class ConvertAbstractValue {

    public static String converetToCodeName(Class<?> aClass) {
        if (aClass == ConstantValue.class) {
            return "Константа";
        }
        ;
        if (aClass == CustomRule.class) {
            return "Константа";
        }
        ;
        if (aClass == DocumentAttribute.class) {
            return "Атрибут документа";
        }
        ;
        if (aClass == GroupRule.class) {
            return "Группа правил";
        }
        ;

        return "N\\A";
    }

}

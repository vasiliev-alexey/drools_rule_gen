package com.av.ui.tm.renders;

import com.av.domain.AbstractValue;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * Created by vasil on 20.10.2015.
 */
public class AbstractValueRender extends BasicComboBoxRenderer {


    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        JLabel lb = new JLabel();


        if (value != null) {
            AbstractValue item = (AbstractValue) value;

            lb.setText(item.getCode()
            );
            setText("" + item.getName());
        }


        if (index == -1) {
            AbstractValue item = (AbstractValue) value;
            lb.setText("");
        }


        return this;
    }
}

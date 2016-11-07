package com.av.ui.tm.renders;

import com.av.domain.Event;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * Created by alexey on 18.10.15.
 */
public class EventRender extends BasicComboBoxRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        if (value != null) {
            Event item = (Event) value;
            setText("" + item.getName());
        }

        if (index == -1) {
            Event item = (Event) value;
            if (item != null) setText(item.getCode());
        }


        return this;
    }


}
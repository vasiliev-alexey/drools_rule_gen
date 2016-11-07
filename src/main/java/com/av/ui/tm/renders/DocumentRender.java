package com.av.ui.tm.renders;

import com.av.domain.Document;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * Created by alexey on 18.10.15.
 */
public class DocumentRender extends BasicComboBoxRenderer {


    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        JLabel lb = new JLabel();


        if (value != null) {
            Document item = (Document) value;

            lb.setText(item.getCode()
            );
            setText("" + item.getName());
        }


        if (index == -1) {
            Document item = (Document) value;
            lb.setText("");
        }


        return this;
    }
}

package com.av.ui.tm.renders;

import com.av.domain.Condition;
import com.av.domain.Document;
import com.av.ui.frames.ConditionFrame;
import com.av.ui.tables.GroupRuleItemTable;
import com.av.ui.tm.GroupRuleItemTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by alexey on 24.10.15.
 */
@org.springframework.stereotype.Component
@Scope("prototype")
public class ButtonEditor extends DefaultCellEditor {
    @Autowired
    ConditionFrame conditionFrame;

    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private int r;
    private int c;
    private Condition condition;

    private Document doc;

    public ButtonEditor() {
        super(new JCheckBox());
        button = new JButton("Условия");
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {


                //  System.out.println("rc=" + r + c);
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        c = column;
        r = row;


        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : "Условия";
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            GroupRuleItemTable tb = (GroupRuleItemTable) table;
            GroupRuleItemTableModel model = (GroupRuleItemTableModel) tb.getModel();
            condition = model.getCondition(r);

            if (condition == null) {
                condition = new Condition();
            }

            conditionFrame.setCondition(condition);

            //  System.out.println(doc);
            conditionFrame.setDoc(doc);
            conditionFrame.setModal(true);
            conditionFrame.afterSetProp();
            conditionFrame.setVisible(true);

        }
        isPushed = false;
        return condition;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
package com.av.ui.tables;

import com.av.ui.tm.GroupRuleItemTableModel;
import com.av.ui.tm.renders.AbstractValueRender;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.util.HashMap;

/**
 * Created by alexey on 24.10.15.
 */
public class GroupRuleItemTable extends JTable {
    private HashMap<String, DefaultCellEditor> editors;

    private AbstractValueRender abstractValueRender = new AbstractValueRender();

    public GroupRuleItemTable(TableModel model) {

        super(model);
        JComboBox<String> valueTypeCombo = new JComboBox<String>();
        valueTypeCombo.addItem("Атрибут документа");
        valueTypeCombo.addItem("Константа");
        valueTypeCombo.addItem("Источник");

        getColumnModel().getColumn(GroupRuleItemTableModel.VALUE_TYPE).setCellEditor(new DefaultCellEditor(valueTypeCombo));

    }


    @Override
    public TableCellEditor getCellEditor(int row, int column) {

        GroupRuleItemTableModel model = (GroupRuleItemTableModel) getModel();

        int modelColumn = convertColumnIndexToModel(column);
        int modelRow = convertRowIndexToModel(row);
        if (modelColumn == GroupRuleItemTableModel.VALUE) {
            Object o = model.getValueAt(modelRow, GroupRuleItemTableModel.VALUE_TYPE);


            String s = (String) o;
            System.out.println("o=" + o);
            if (editors.containsKey(s)) {
                return editors.get(s);
            }


        }


        return super.getCellEditor(row, column);
    }

    public void setEditors(HashMap<String, DefaultCellEditor> editors) {
        this.editors = editors;
    }


}

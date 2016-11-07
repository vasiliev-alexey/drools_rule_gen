package com.av.ui.tables;

import com.av.ui.tm.SegmentSettingTableModel;
import com.av.ui.tm.renders.AbstractValueRender;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.util.HashMap;

/**
 * Created by alexey on 21.10.15.
 */
public class SegmentTable extends JTable {


    private HashMap<String, DefaultCellEditor> editors;

    private AbstractValueRender abstractrender = new AbstractValueRender();

    public SegmentTable(TableModel model) {
        super(model);
        JComboBox<String> ruleTypeCombo = new JComboBox<String>();

        ruleTypeCombo.addItem("Атрибут документа");
        ruleTypeCombo.addItem("Константа");
        ruleTypeCombo.addItem("Источник");
        ruleTypeCombo.addItem("Группа правил");
        getColumnModel().getColumn(SegmentSettingTableModel.RULE_TYPE).setCellEditor(new DefaultCellEditor(ruleTypeCombo));


    }

    public void setEditors(HashMap<String, DefaultCellEditor> editors) {
        this.editors = editors;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {

        SegmentSettingTableModel model = (SegmentSettingTableModel) this.getModel();
        int modelColumn = convertColumnIndexToModel(column);
        int modelRow = convertRowIndexToModel(row);

        if (modelColumn == SegmentSettingTableModel.RULE_CODE) {
            Object o;

            o = model.getValueAt(modelRow, SegmentSettingTableModel.RULE_TYPE);


            String s = (String) o;
            if (editors.containsKey(s)) {
                return editors.get(s);
            }


        }


        return super.getCellEditor(row, column);
    }


}

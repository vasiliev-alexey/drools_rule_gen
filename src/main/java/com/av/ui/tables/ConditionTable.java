package com.av.ui.tables;

import com.av.ui.tm.ConditionLineTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.util.HashMap;

/**
 * Created by vasil on 20.10.2015.
 */
public class ConditionTable extends JTable {

    private HashMap<String, DefaultCellEditor> editors = new HashMap<String, DefaultCellEditor>();


    public ConditionTable(TableModel model) {
        super(model);
    }

    ;


    @Override
    public TableCellEditor getCellEditor(int row, int column) {

        ConditionLineTableModel model = (ConditionLineTableModel) getModel();

        int modelColumn = convertColumnIndexToModel(column);
        int modelRow = convertRowIndexToModel(row);
        if (modelColumn == ConditionLineTableModel.LEFT_OBJECT || modelColumn == ConditionLineTableModel.RIGHT_OBJECT) {
            Object o;
            if (modelColumn == ConditionLineTableModel.LEFT_OBJECT) {
                o = model.getValueAt(modelRow, ConditionLineTableModel.LEFT_OBJECT_TYPE);
            } else {
                o = model.getValueAt(modelRow, ConditionLineTableModel.RIGHT_OBJECT_TYPE);
            }

            String s = (String) o;
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

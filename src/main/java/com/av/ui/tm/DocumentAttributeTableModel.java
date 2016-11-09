package com.av.ui.tm;

import com.av.domain.DocumentAttribute;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 18.10.15.
 */
public class DocumentAttributeTableModel extends AbstractTableModel {

    private List<DocumentAttribute> listAttrDoc = new ArrayList<DocumentAttribute>();
    public static final int COLUMN_CODEUN = 5;
    public static final int CODE = 0;
    public static final int NAME = 1;
    public static final int PATH = 2;
    public static final int FIELD_CLASS = 3;
    public static final int FIELD_CODE = 4;


    public void setList(List<DocumentAttribute> list) {

        this.listAttrDoc = list;
    }

    public int getRowCount() {
        if (listAttrDoc == null) return 0;

        return listAttrDoc.size();
    }

    public int getColumnCount() {
        return COLUMN_CODEUN;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (listAttrDoc.size() == 0) return null;

        switch (columnIndex) {
            case CODE:
                return listAttrDoc.get(rowIndex).getCode();
            case NAME:
                return listAttrDoc.get(rowIndex).getName();
            case PATH:
                return listAttrDoc.get(rowIndex).getPath();
            case FIELD_CLASS:
                return listAttrDoc.get(rowIndex).getFieldClass();
            case FIELD_CODE:
                return listAttrDoc.get(rowIndex).getFieldCode();
        }

        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case CODE:
                return "Код документа";
            case NAME:
                return "Наименование документа";
            case PATH:
                return "Путь доступа";
            case FIELD_CLASS:
                return "Класс атрибута";
            case FIELD_CODE:
                return "Код класса";
        }

        return null;
    }
}

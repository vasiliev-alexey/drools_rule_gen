package com.av.ui.tm;

import com.av.domain.Document;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by alexey on 18.10.15.
 */
public class DocumentTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 3;
    private static final int CODE = 0;
    private static final int NAME = 1;
    private  static final  int PACKAGE = 2;


    private List<Document> listDoc;

    public void setList(List<Document> listDoc) {

        this.listDoc = listDoc;

    }

    ;

    public int getRowCount() {
        return listDoc.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        if (listDoc.size() == 0) return null;

        switch (columnIndex) {
            case CODE:
                return listDoc.get(rowIndex).getCode();
            case NAME:
                return listDoc.get(rowIndex).getName();
            case PACKAGE:
                return  listDoc.get(rowIndex).getPackagePath();
        }

        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case CODE:
                return "��� ���������";
            case NAME:
                return "������������ ���������";
            case PACKAGE:
                return  "�����";
        }

        return null;
    }
}

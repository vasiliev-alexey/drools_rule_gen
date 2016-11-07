package com.av.ui.tm.renders;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by alexey on 24.10.15.
 */
public class ConditionButtonRender implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (JButton) value;
    }
}

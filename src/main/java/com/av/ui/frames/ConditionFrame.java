package com.av.ui.frames;

import com.av.dao.CustomRuleDao;
import com.av.domain.*;
import com.av.ui.tables.ConditionTable;
import com.av.ui.tm.ConditionLineTableModel;
import com.av.ui.tm.renders.AbstractValueRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vasil on 19.10.2015.
 */
@Component
@Scope("prototype")
@Lazy
public class ConditionFrame extends JDialog {

    @Autowired
    private CustomRuleDao customRuleDao;

    // private EventSetting setting;
    // private Event event;
    private Document doc;
    private JPanel topPanel;
    private Condition condition;
    private JTextArea condName = new JTextArea(5, 80);
    private ConditionLineTableModel tbLineModel;
    private JComboBox<DocumentAttribute> attrDocCombo;
    private JComboBox<CustomRule> ruleCodesCombo = new JComboBox<CustomRule>();

    public void afterSetProp() {
        // topPanel.add(new JLabel("Настройка показателя с кодом " + setting.getCode()));
        if (condition != null) {

            tbLineModel.setListConditionLine(condition.getConditionLineList());
            condName.setText(condition.getName());
            tbLineModel.fireTableDataChanged();
        }
        attrDocCombo.removeAllItems();

        for (DocumentAttribute docAtr : doc.getDocumentAttributeList()) {
            attrDocCombo.addItem(docAtr);
        }
        ;


        List<CustomRule> custRileList = customRuleDao.findAllByDoc(doc.getId());

        ruleCodesCombo.removeAllItems();
        for (CustomRule c : custRileList) {
            ruleCodesCombo.addItem(c);
        }


        topPanel.repaint();

    }

    @PostConstruct
    public void init() {
        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Настройка условия срабатывания строк"));
        topPanel.add(new JLabel("Описание настройки"));
        topPanel.add(condName);

        condName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                condition.setName(condName.getText());
            }


        });

        JButton btAddCondLine = new JButton("Добавить условие");
        topPanel.add(btAddCondLine);


        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Конструктор условий"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        setLayout(new BorderLayout());
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);
        add(splitPane);
        setPreferredSize(new Dimension(1000, 800));
        setMinimumSize(getPreferredSize());

        tbLineModel = new ConditionLineTableModel();

        final ConditionTable tbCondLine = new ConditionTable(tbLineModel);


        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.LEFT_BRACKET).setMaxWidth(3);
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.RIGHT_BRACKET).setMaxWidth(3);
        // tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.OPERATOR).setMaxWidth(15);
        // tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.USER_SEQ).setMaxWidth(15);
        //  tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.USER_SEQ).setCellEditor(new NumberCellEditor());

        JComboBox<String> leftCombo = new JComboBox<String>(new String[]{"", "("});
        JComboBox<String> rightCombo = new JComboBox<String>(new String[]{"", ")"});
        JComboBox<String> leftType = new JComboBox<String>(new String[]{"", "Атрибут документа", "Источник"});
        final JComboBox<String> rightType = new JComboBox<String>(new String[]{"", "Атрибут документа", "Источник", "Константа"});


        rightType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                if (rightType.getSelectedItem() == null || rightType.getSelectedItem().equals("")) {
                    int r = tbCondLine.getSelectedRow();
                    int t = tbCondLine.convertRowIndexToModel(r);


                    tbLineModel.setValueAt(null, t, tbLineModel.RIGHT_OBJECT);
                    tbLineModel.fireTableDataChanged();
                    //   System.out.println("set null");


                }

            }
        });

        JComboBox<String> expression = new JComboBox<String>(Expression.getExpressions());

        JComboBox<String> operCombo = new JComboBox<String>(new String[]{"", "И", "ИЛИ"});
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.LEFT_BRACKET).setCellEditor(new DefaultCellEditor(leftCombo));
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.RIGHT_BRACKET).setCellEditor(new DefaultCellEditor(rightCombo));
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.EXPRESSION).setCellEditor(new DefaultCellEditor(expression));
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.OPERATOR).setCellEditor(new DefaultCellEditor(operCombo));
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.LEFT_OBJECT_TYPE).setCellEditor(new DefaultCellEditor(leftType));
        tbCondLine.getColumnModel().getColumn(ConditionLineTableModel.RIGHT_OBJECT_TYPE).setCellEditor(new DefaultCellEditor(rightType));


        final HashMap<String, DefaultCellEditor> editors = new HashMap<String, DefaultCellEditor>();
        tbCondLine.setEditors(editors);
        attrDocCombo = new JComboBox<DocumentAttribute>();

        attrDocCombo.setRenderer(new AbstractValueRender());
        ruleCodesCombo.setRenderer(new AbstractValueRender());


        editors.put("Источник", new DefaultCellEditor(ruleCodesCombo));
        editors.put("Атрибут документа", new DefaultCellEditor(attrDocCombo));
        editors.put("Константа", new DefaultCellEditor(new JTextField()));


        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(tbCondLine), BorderLayout.CENTER);

        btAddCondLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (condition.getConditionLineList() == null) {
                    condition.setConditionLineList(new ArrayList<ConditionLine>());
                    tbLineModel.setListConditionLine(condition.getConditionLineList());
                }

                condition.getConditionLineList().add(new ConditionLine());
                //System.out.println("Chandged");
                tbLineModel.fireTableDataChanged();
                bottomPanel.repaint();
            }
        });


        validate();


    }


    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
}

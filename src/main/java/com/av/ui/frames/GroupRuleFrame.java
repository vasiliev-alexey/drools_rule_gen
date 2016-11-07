package com.av.ui.frames;

import com.av.dao.CustomRuleDao;
import com.av.dao.GroupRuleDAO;
import com.av.domain.*;
import com.av.ui.tables.GroupRuleItemTable;
import com.av.ui.tm.GroupRuleItemTableModel;
import com.av.ui.tm.GroupRuleTableModel;
import com.av.ui.tm.renders.AbstractValueRender;
import com.av.ui.tm.renders.ButtonEditor;
import com.av.ui.tm.renders.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by alexey on 23.10.15.
 */
@Component
public class GroupRuleFrame extends JDialog {

    @Autowired
    private GroupRuleDAO groupRuleDAO;
    @Autowired
    private CustomRuleDao customRuleDao;
    @Autowired
    private ButtonEditor buttonEditor;

    private Document doc;
    private JSplitPane splitPane;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel managePanel;
    private List<GroupRule> listGroupRule;
    private GroupRuleTableModel ruleGroupModel = new GroupRuleTableModel();
    private GroupRuleItemTableModel ruleGroupItemModel = new GroupRuleItemTableModel();
    private JComboBox<CustomRule> ruleCodesCombo = new JComboBox<CustomRule>();
    private JComboBox<DocumentAttribute> attrDocCombo = new JComboBox<DocumentAttribute>();


    public void showRuleGroupForm(Document doc) {
        this.doc = doc;
        listGroupRule = groupRuleDAO.findAllByDocId(doc.getId());
        ruleGroupModel.setRuleGroupList(listGroupRule);


        attrDocCombo.removeAllItems();
        for (DocumentAttribute docAtr : doc.getDocumentAttributeList()) {
            attrDocCombo.addItem(docAtr);
        }
        ;
        ruleCodesCombo.removeAllItems();
        for (CustomRule cr : customRuleDao.findAllByDoc(doc.getId())) {
            ruleCodesCombo.addItem(cr);
        }

        buttonEditor.setDoc(doc);
        setVisible(true);
    }


    @PostConstruct
    public void init() {

        setPreferredSize(new Dimension(1000, 700));
        setMinimumSize(getPreferredSize());
        splitPane = new JSplitPane();
        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Группы правил вычисления счетов"));
        topPanel.setLayout(new BorderLayout());

        final JTable topTable = new JTable();
        topTable.setModel(ruleGroupModel);
        JComboBox<Integer> comboSegNum = new JComboBox();
        for (int i = 1; i < 4; i++) {
            comboSegNum.addItem(i);
        }
        topTable.getColumnModel().getColumn(GroupRuleTableModel.SEGMENT_NUM)
                .setCellEditor(new DefaultCellEditor(comboSegNum));

        managePanel = new JPanel();
        managePanel.setLayout(new BoxLayout(managePanel, BoxLayout.X_AXIS));
        JButton btAdd = new JButton("Добавить группу правил");
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GroupRule gr = new GroupRule();
                gr.setDoc(doc);
                listGroupRule.add(gr);
                ruleGroupModel.fireTableDataChanged();
                ruleGroupItemModel.fireTableDataChanged();

            }
        });
        JButton btSave = new JButton("Сохранить группы правил");
        btSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (GroupRule gr : listGroupRule) {
                    groupRuleDAO.saveGroupRule(gr);
                }
            }
        });

        managePanel.setBorder(BorderFactory.createTitledBorder("Управление"));
        managePanel.add(btAdd);
        managePanel.add(btSave);
        topPanel.add(managePanel, BorderLayout.SOUTH);
        topPanel.add(new JScrollPane(topTable), BorderLayout.CENTER);


        topTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int r = topTable.getSelectedRow();
                int t = topTable.convertRowIndexToModel(r);

                if (listGroupRule.get(t).getItems() != null) {
                    ruleGroupItemModel.setGroupRuleItemList(listGroupRule.get(t).getItems());
                    ruleGroupItemModel.fireTableDataChanged();
                }

            }
        });


        bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Настройка правил для группы"));

        bottomPanel.setLayout(new BorderLayout());
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setResizeWeight(0.33D);


        setTitle("Настройка группы правил вычисления счета");
        add(splitPane);

        ////////////////////// bottomPanel //////////////////////////


        final GroupRuleItemTable bottomTable = new GroupRuleItemTable(ruleGroupItemModel);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(bottomTable), BorderLayout.CENTER);


        JButton btAddItem = new JButton("Добавить правило");
        managePanel.add(btAddItem);
        btAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int r = topTable.getSelectedRow();
                int t = topTable.convertRowIndexToModel(r);

                if (listGroupRule.get(t).getItems() == null) {
                    listGroupRule.get(t).setItems(new ArrayList<GroupRuleItem>());
                }

                listGroupRule.get(t).getItems().add(new GroupRuleItem());
                ruleGroupItemModel.fireTableDataChanged();

            }
        });

        attrDocCombo = new JComboBox<DocumentAttribute>();
        attrDocCombo.setRenderer(new AbstractValueRender());
        ruleCodesCombo.setRenderer(new AbstractValueRender());


        HashMap<String, DefaultCellEditor> editors = new HashMap<String, DefaultCellEditor>();
        editors.put("Источник", new DefaultCellEditor(ruleCodesCombo));
        editors.put("Атрибут документа", new DefaultCellEditor(attrDocCombo));
        editors.put("Константа", new DefaultCellEditor(new JTextField()));
        bottomTable.setEditors(editors);

        bottomTable.getColumnModel().getColumn(GroupRuleItemTableModel.CONDITION).setCellRenderer(new ButtonRenderer());
        bottomTable.getColumnModel().getColumn(GroupRuleItemTableModel.CONDITION).setCellEditor(buttonEditor);


        JButton btDelRule = new JButton("Удалить правило");
        managePanel.add(btDelRule);
        btDelRule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int r = bottomTable.getSelectedRow();
                int t = bottomTable.convertRowIndexToModel(r);

                int tr = topTable.getSelectedColumn();
                int tt = topTable.convertRowIndexToModel(tr);


                if (listGroupRule.get(tt).getItems() != null) {
                    listGroupRule.get(tt).getItems().remove(t) ;
                    ruleGroupItemModel.fireTableDataChanged();
                }



            }
        });

        setModal(true);
        validate();

    }


}

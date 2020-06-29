package com.luoboduner.wesync.ui.panel;

import com.luoboduner.wesync.App;
import com.luoboduner.wesync.enums.DangerOperationEnum;
import com.luoboduner.wesync.logic.sync.DataBaseInfo;
import com.luoboduner.wesync.logic.sync.DataBaseSync;
import com.luoboduner.wesync.tools.ConstantsTools;
import com.luoboduner.wesync.tools.DESPlus;
import com.luoboduner.wesync.tools.DbUtilMySQL;
import com.luoboduner.wesync.tools.PropertyUtil;
import com.luoboduner.wesync.ui.UiConsts;
import com.luoboduner.wesync.ui.component.MyIconButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * 目标数据库面板
 *
 * @author Bob
 */
public class DatabasePanelTo extends JPanel{

    private static final long serialVersionUID = 1L;

    public static MyIconButton buttonStartSync;
    private static MyIconButton buttonTestLink;

    private static JTextField textFieldDatabaseHost;
    private static JTextField textFieldDatabaseName;
    private static JTextField textFieldDatabaseUser;
    private static JPasswordField passwordFieldDatabasePassword;
    private static ButtonGroup buttonGroup;
    private static JRadioButton radioBtn01;
    private static JRadioButton radioBtn02;
    private static JProgressBar progressBar;

    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;

    public static int currentProgress = MIN_PROGRESS;
    //默认只同步全量数据
    private static int dangerOpt = DangerOperationEnum.FULL_DATA_ONLY.ordinal();

    private static final Logger logger = LoggerFactory.getLogger(DatabasePanelTo.class);

    /**
     * 构造
     */
    public DatabasePanelTo() {
        initialize();
        addComponent();
        setContent();
        addListener();
    }

    /**
     * 初始化
     */
    private void initialize() {
        this.setBackground(UiConsts.MAIN_BACK_COLOR);
        this.setLayout(new BorderLayout());
    }

    /**
     * 添加组件
     */
    private void addComponent() {

        this.add(getCenterPanel(), BorderLayout.CENTER);
        this.add(getDownPanel(), BorderLayout.SOUTH);

    }

    /**
     * 中部面板
     *
     * @return
     */
    private JPanel getCenterPanel() {
        // 中间面板
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(UiConsts.MAIN_BACK_COLOR);
        panelCenter.setLayout(new GridLayout(2, 1));

        // 设置Grid
        JPanel panelGridSetting = new JPanel();
        panelGridSetting.setBackground(UiConsts.MAIN_BACK_COLOR);
        panelGridSetting.setLayout(new FlowLayout(FlowLayout.LEFT, UiConsts.MAIN_H_GAP, 5));

        // 初始化组件
        JLabel labelDatabaseType = new JLabel(PropertyUtil.getProperty("ds.ui.database.type"));
        JLabel labelDatabaseHost = new JLabel(PropertyUtil.getProperty("ds.ui.database.host"));
        JLabel labelDatabaseName = new JLabel(PropertyUtil.getProperty("ds.ui.database.name"));
        JLabel labelDatabaseUser = new JLabel(PropertyUtil.getProperty("ds.ui.database.user"));
        JLabel labelDatabasePassword = new JLabel(PropertyUtil.getProperty("ds.ui.database.password"));
        JLabel labelDangerOperation = new JLabel(PropertyUtil.getProperty("ds.ui.database.dangerOpt"));

        JComboBox<String> comboxDatabaseType = new JComboBox<>();
        comboxDatabaseType.addItem("MySQL");
        comboxDatabaseType.setEditable(false);

        textFieldDatabaseHost = new JTextField();
        textFieldDatabaseName = new JTextField();
        textFieldDatabaseUser = new JTextField();
        passwordFieldDatabasePassword = new JPasswordField();
        progressBar = new JProgressBar();
        // 设置进度的 最小值 和 最大值
        progressBar.setMinimum(MIN_PROGRESS);
        progressBar.setMaximum(MAX_PROGRESS);
        progressBar.setStringPainted(true);

        // 创建按钮组，把两个单选按钮添加到该组
        buttonGroup= new ButtonGroup();
        // 创建两个单选按钮
        radioBtn01 = new JRadioButton(PropertyUtil.getProperty("ds.ui.database.danger.data"));
        radioBtn02 = new JRadioButton(PropertyUtil.getProperty("ds.ui.database.danger.tableData"));
        buttonGroup.add(radioBtn01);
        buttonGroup.add(radioBtn02);

        // 设置第一个单选按钮选中
        radioBtn01.setSelected(true);
        radioBtn01.setOpaque(false);
        radioBtn02.setOpaque(false);

        // 字体
        labelDatabaseType.setFont(UiConsts.FONT_NORMAL);
        labelDatabaseHost.setFont(UiConsts.FONT_NORMAL);
        labelDatabaseName.setFont(UiConsts.FONT_NORMAL);
        labelDatabaseUser.setFont(UiConsts.FONT_NORMAL);
        labelDatabasePassword.setFont(UiConsts.FONT_NORMAL);
        labelDangerOperation.setFont(UiConsts.FONT_NORMAL);

        comboxDatabaseType.setFont(UiConsts.FONT_NORMAL);
        textFieldDatabaseHost.setFont(UiConsts.FONT_NORMAL);
        textFieldDatabaseName.setFont(UiConsts.FONT_NORMAL);
        textFieldDatabaseUser.setFont(UiConsts.FONT_NORMAL);
        passwordFieldDatabasePassword.setFont(UiConsts.FONT_NORMAL);
        progressBar.setFont(UiConsts.FONT_NORMAL);
        radioBtn01.setFont(UiConsts.FONT_NORMAL);
        radioBtn02.setFont(UiConsts.FONT_NORMAL);

        // 大小
        labelDatabaseType.setPreferredSize(UiConsts.LABLE_SIZE_ITEM);
        labelDatabaseHost.setPreferredSize(UiConsts.LABLE_SIZE_ITEM);
        labelDatabaseName.setPreferredSize(UiConsts.LABLE_SIZE_ITEM);
        labelDatabaseUser.setPreferredSize(UiConsts.LABLE_SIZE_ITEM);
        labelDatabasePassword.setPreferredSize(UiConsts.LABLE_SIZE_ITEM);

        comboxDatabaseType.setPreferredSize(UiConsts.TEXT_FIELD_SIZE_ITEM);
        textFieldDatabaseHost.setPreferredSize(UiConsts.TEXT_FIELD_SIZE_ITEM);
        textFieldDatabaseName.setPreferredSize(UiConsts.TEXT_FIELD_SIZE_ITEM);
        textFieldDatabaseUser.setPreferredSize(UiConsts.TEXT_FIELD_SIZE_ITEM);
        passwordFieldDatabasePassword.setPreferredSize(UiConsts.TEXT_FIELD_SIZE_ITEM);
        progressBar.setPreferredSize(UiConsts.TEXT_FIELD_SIZE_ITEM);

        // 组合元素
        panelGridSetting.add(labelDatabaseType);
        panelGridSetting.add(comboxDatabaseType);

        panelGridSetting.add(labelDatabaseHost);
        panelGridSetting.add(textFieldDatabaseHost);

        panelGridSetting.add(labelDatabaseName);
        panelGridSetting.add(textFieldDatabaseName);

        panelGridSetting.add(labelDatabaseUser);
        panelGridSetting.add(textFieldDatabaseUser);

        panelGridSetting.add(labelDatabasePassword);
        panelGridSetting.add(passwordFieldDatabasePassword);

        panelGridSetting.add(labelDangerOperation);
        panelGridSetting.add(radioBtn01);
        panelGridSetting.add(radioBtn02);

        // 设置进度条Grid
        JPanel panelGridProgress = new JPanel();
        panelGridProgress.setBackground(UiConsts.MAIN_BACK_COLOR);
        panelGridProgress.setLayout(new FlowLayout(FlowLayout.LEFT, UiConsts.MAIN_H_GAP, 5));
        JLabel labelSyncProgress = new JLabel(PropertyUtil.getProperty("ds.ui.database.sync.progress"));
        labelSyncProgress.setFont(UiConsts.FONT_NORMAL);
        labelSyncProgress.setPreferredSize(UiConsts.LABLE_SIZE_ITEM);
        panelGridProgress.add(labelSyncProgress);
        panelGridProgress.add(progressBar);


        panelCenter.add(panelGridSetting);
        panelCenter.add(panelGridProgress);
        return panelCenter;
    }

    /**
     * 底部面板
     *
     * @return
     */
    private JPanel getDownPanel() {
        JPanel panelDown = new JPanel();

        panelDown.setBackground(UiConsts.MAIN_BACK_COLOR);
        panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, UiConsts.MAIN_H_GAP, 5));

        buttonTestLink = new MyIconButton(UiConsts.ICON_TEST_LINK, UiConsts.ICON_TEST_LINK_ENABLE,
                UiConsts.ICON_TEST_LINK_DISABLE, "");
        buttonStartSync = new MyIconButton(UiConsts.ICON_SYNC_NOW, UiConsts.ICON_SYNC_NOW_ENABLE,
                UiConsts.ICON_SYNC_NOW_DISABLE, "开始同步数据，需要等待一会儿");


        panelDown.add(buttonTestLink);
        panelDown.add(buttonStartSync);

        return panelDown;
    }

    /**
     * 设置文本区内容
     */
    public static void setContent() {
        textFieldDatabaseHost.setText(ConstantsTools.CONFIGER.getHostTo());
        textFieldDatabaseName.setText(ConstantsTools.CONFIGER.getNameTo());

        String user = "";
        String password = "";
        try {
            DESPlus des = new DESPlus();
            user = des.decrypt(ConstantsTools.CONFIGER.getUserTo());
            password = des.decrypt(ConstantsTools.CONFIGER.getPasswordTo());
        } catch (Exception e) {
            logger.error(PropertyUtil.getProperty("ds.ui.database.to.err.decode") + e.toString());
            e.printStackTrace();
        }
        textFieldDatabaseUser.setText(user);
        passwordFieldDatabasePassword.setText(password);

    }

    /**
     * 为相关组件添加事件监听
     */
    private void addListener() {
        radioBtn01.addActionListener(e ->{
            dangerOpt = DangerOperationEnum.FULL_DATA_ONLY.ordinal();
        });

        radioBtn02.addActionListener(e ->{
            dangerOpt = DangerOperationEnum.FULL_DATA_AND_TABLE.ordinal();
        });

        buttonStartSync.addChangeListener(Chan -> {

        });

        buttonStartSync.addActionListener(e -> {
            try {


                ConstantsTools.CONFIGER.setHostTo(textFieldDatabaseHost.getText());
                ConstantsTools.CONFIGER.setNameTo(textFieldDatabaseName.getText());

                String password = "";
                String user = "";
                try {
                    DESPlus des = new DESPlus();
                    user = des.encrypt(textFieldDatabaseUser.getText());
                    password = des.encrypt(new String(passwordFieldDatabasePassword.getPassword()));

                } catch (Exception e1) {
                    logger.error(PropertyUtil.getProperty("ds.ui.database.to.err.encode") + e1.toString());
                    e1.printStackTrace();
                }
                ConstantsTools.CONFIGER.setUserTo(user);
                ConstantsTools.CONFIGER.setPasswordTo(password);

                DataBaseInfo dbFrom = ConstantsTools.CONFIGER.getDataBaseFrom();
                DataBaseInfo dbTo = ConstantsTools.CONFIGER.getDataBaseTo();


                DataBaseSync dataBaseSync = new DataBaseSync(dbFrom,dbTo,dangerOpt,currentProgress,10000);
                java.util.List<String> msgList = dataBaseSync.syncDataBase();
                currentProgress = 100;
                progressBar.setValue(currentProgress);

                JOptionPane.showMessageDialog(App.databasePanel,String.join("\n", msgList), PropertyUtil.getProperty("ds.ui.tips"), JOptionPane.PLAIN_MESSAGE);
                buttonStartSync.setEnabled(true);
                currentProgress = MIN_PROGRESS;
            } catch (Exception e1) {
                buttonStartSync.setEnabled(true);
                JOptionPane.showMessageDialog(App.databasePanel, PropertyUtil.getProperty("ds.ui.save.fail") + e1.getMessage(), PropertyUtil.getProperty("ds.ui.tips"), JOptionPane.ERROR_MESSAGE);
                logger.error("Write to xml file error" + e1.toString());
                e1.printStackTrace();

            }
        });

        buttonTestLink.addActionListener(e -> {

            try {
                DbUtilMySQL dbMySQL = DbUtilMySQL.getInstance();
                String dburl = textFieldDatabaseHost.getText();
                String dbname = textFieldDatabaseName.getText();
                String dbuser = textFieldDatabaseUser.getText();
                String dbpassword = new String(passwordFieldDatabasePassword.getPassword());
                Connection conn = dbMySQL.testConnection(dburl, dbname, dbuser, dbpassword);
                if (conn == null) {
                    JOptionPane.showMessageDialog(App.databasePanel, PropertyUtil.getProperty("ds.ui.database.err.link.fail"), PropertyUtil.getProperty("ds.ui.tips"),
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(App.databasePanel, PropertyUtil.getProperty("ds.ui.database.err.link.success"), PropertyUtil.getProperty("ds.ui.tips"),
                            JOptionPane.PLAIN_MESSAGE);
                }

            } catch (Exception e1) {
                JOptionPane.showMessageDialog(App.databasePanel, PropertyUtil.getProperty("ds.ui.database.err.link.fail") + e1.getMessage(), PropertyUtil.getProperty("ds.ui.tips"),
                        JOptionPane.ERROR_MESSAGE);
            }

        });

    }
}

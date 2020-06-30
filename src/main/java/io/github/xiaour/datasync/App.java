package io.github.xiaour.datasync;

import io.github.xiaour.datasync.tools.PropertyUtil;
import io.github.xiaour.datasync.ui.UiConsts;
import io.github.xiaour.datasync.ui.dialog.DbBackUpCreateDialog;
import io.github.xiaour.datasync.ui.panel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 程序入口，主窗口Frame
 *
 * @author Bob
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private JFrame frame;

    public static JPanel mainPanelCenter;

    public static DatabasePanel databasePanel;
    public static SchedulePanel schedulePanel;
    public static SettingPanel settingPanel;
    /**
     * 新建备份dialog
     */
    public static DbBackUpCreateDialog dbBackUpCreateDialog;

    /**
     * 程序入口main
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App window = new App();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 构造，创建APP
     */
    public App() {
        initialize();
    }

    /**
     * 初始化frame内容
     */
    private void initialize() {
        logger.info("==================AppInitStart");
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // 初始化主窗口
        frame = new JFrame();
        frame.setBounds(UiConsts.MAIN_WINDOW_X, UiConsts.MAIN_WINDOW_Y, UiConsts.MAIN_WINDOW_WIDTH,
                UiConsts.MAIN_WINDOW_HEIGHT);
        frame.setIconImage(UiConsts.ICON_DATA_SYNC.getImage());
        frame.setTitle(UiConsts.APP_NAME);
        frame.setBackground(UiConsts.MAIN_BACK_COLOR);
        JPanel mainPanel = new JPanel(true);
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        ToolBarPanel toolbar = new ToolBarPanel();
        databasePanel = new DatabasePanel();
        settingPanel = new SettingPanel();

        mainPanel.add(toolbar, BorderLayout.WEST);

        mainPanelCenter = new JPanel(true);
        mainPanelCenter.setLayout(new BorderLayout());
        mainPanelCenter.add(databasePanel, BorderLayout.CENTER);

        mainPanel.add(mainPanelCenter, BorderLayout.CENTER);

        // 添加数据库备份对话框
        //addDialog();

        frame.add(mainPanel);

        frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (!DatabasePanelTo.buttonStartSync.isEnabled()) {
                    JOptionPane.showMessageDialog(App.databasePanel,
                            PropertyUtil.getProperty("ds.ui.mainwindow.exitconfirm"), "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        logger.info("==================AppInitEnd");
    }

    /**
     * 数据库备份对话框
     */
    private void addDialog() {
        // 数据库备份对话框
        dbBackUpCreateDialog = new DbBackUpCreateDialog(frame, PropertyUtil.getProperty("ds.ui.mainwindow.dialog.newBackUp"), true);
        dbBackUpCreateDialog.init();
    }

}
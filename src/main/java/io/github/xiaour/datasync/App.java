package io.github.xiaour.datasync;

import com.apple.eawt.Application;
import io.github.xiaour.datasync.tools.PropertyUtil;
import io.github.xiaour.datasync.ui.UiConsts;
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
 * @author Bob,Zhangtao
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static JFrame frame = new JFrame();

    public static JPanel mainPanelCenter;

    public static DatabasePanel databasePanel;
    public static SchedulePanel schedulePanel;
    public static SettingPanel settingPanel;

    /**
     * 程序入口main
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new App();
                App.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 构造，创建APP
     */
    public App() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        initialize();
    }

    /**
     * 初始化frame内容
     */
    private void initialize() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        logger.info("==================AppInitStart");
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        //获得操作系统
        String OsName = System.getProperty("os.name");
        //是mac 就设置dock图标
        if (OsName.contains("Mac")) {
            //指定mac 的dock图标
            Application app = Application.getApplication();
            app.setDockIconImage(UiConsts.ICON_DATA_SYNC.getImage());

            System.setProperty("apple.laf.useScreenMenuBar", "true");

            System.setProperty("com.apple.mrj.application.apple.menu.about.name", UiConsts.APP_NAME);

            String lookAndFeel = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
            try {
                javax.swing.UIManager.setLookAndFeel(lookAndFeel);
            } catch (Exception ex) {

            }
        }

        // 初始化主窗口
        frame.setResizable(false);
        frame.setBounds(UiConsts.MAIN_WINDOW_X, UiConsts.MAIN_WINDOW_Y, UiConsts.MAIN_WINDOW_WIDTH,
                UiConsts.MAIN_WINDOW_HEIGHT);
        frame.setIconImage(UiConsts.ICON_DATA_SYNC.getImage());


        frame.setTitle(UiConsts.APP_NAME);
        frame.setBackground(UiConsts.MAIN_BACK_COLOR);
        frame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(true);
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        ToolBarPanel toolbar = new ToolBarPanel();
        databasePanel = new DatabasePanel();
        settingPanel = new SettingPanel();
        schedulePanel =  new SchedulePanel();

        mainPanel.add(toolbar, BorderLayout.WEST);

        mainPanelCenter = new JPanel(true);
        mainPanelCenter.setLayout(new BorderLayout());
        mainPanelCenter.add(databasePanel, BorderLayout.CENTER);

        mainPanel.add(mainPanelCenter, BorderLayout.CENTER);

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


}
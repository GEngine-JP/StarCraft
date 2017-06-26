package gui;

import core.GridMapRender;
import core.ResourceManager;
import net.NetWorkManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameGUI extends JPanel {

    //卡片布局器
    private CardLayout cardLayout = new CardLayout();
    //进度条面板
    private ProgressPanel progressPanel;
    //菜单面板
    private MainMenuPanel menuPanel;
    //游戏面板
    private GamePanel gamePanel;
    //需要update
    private Abstractpanel currentPanel;
    //单人游戏面板
    private SingleGamePanel singleGamePanel;
    //多人能游戏面板
    private MutilPlayerPanel mutilPlayerPanel;

    NewGamePanel newGamePanel;

    NetWorkManager netWorkManager;

    GridMapRender mapRender;

    JFrame frame;

    public GameGUI(JFrame frame) {
        this.frame = frame;
        setLayout(cardLayout);
        getBorder();
        frame.getContentPane().add(this);
        setSize(frame.getWidth(), frame.getHeight());
        //这里有个次序,最先加入的显示
        currentPanel = menuPanel = new MainMenuPanel(this, "mainMenu");
        progressPanel = new ProgressPanel(this, "progress");
        gamePanel = new GamePanel(this, "game");
        singleGamePanel = new SingleGamePanel(this, "singleGame");
        mutilPlayerPanel = new MutilPlayerPanel(this, "mutilGame");
        newGamePanel = new NewGamePanel(this, "newGame");

    }

    public void switchProgress(final int type, final List<Integer> types) {

        cardLayout.show(this, "progress");
        currentPanel = progressPanel;

        Thread t = new Thread() {
            public void run() {
                mapRender = ResourceManager.load(type, types);
                mapRender.setNetWorkManager(netWorkManager);
            }
        };
        t.start();

    }

    public void switchGame() {

        gamePanel.init();
        cardLayout.show(this, "game");
        currentPanel = gamePanel;
    }

    public void switchSingleGame() {

        cardLayout.show(this, "singleGame");
        singleGamePanel.reset();
        currentPanel = singleGamePanel;
    }

    public void switchMutilGame() {

        cardLayout.show(this, "mutilGame");
        mutilPlayerPanel.reset();
        currentPanel = mutilPlayerPanel;
    }

    public void switchMainMenu() {

        cardLayout.show(this, "mainMenu");
        menuPanel.reset();
        currentPanel = menuPanel;

    }

    public void switchNewGame() {

        cardLayout.show(this, "newGame");
        newGamePanel.reset();
        currentPanel = newGamePanel;

    }


    public void update(long elapsedTime) {

        if (currentPanel != null) {
            currentPanel.update(elapsedTime);
        }

    }
}

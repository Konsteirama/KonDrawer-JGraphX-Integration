/*
 * A ISGCI specific implementation for the startpage of the tabbedpane that is
 * displayed once a new window is opened. It displays logo, most common
 * functions and news.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A ISGCI specific implementation for the startpage of the tabbedpane that is
 * displayed once a new window is opened. It displays logo, most common
 * functions and news.
 */
class StartPanel extends JPanel {

    /**
     * Should be changed each time this class changes.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The website containing the news.
     */
    private static final String NEWSPAGE 
        = "http://92.51.130.117/news.html";
    
    /**
     * How much time should pass between animationframes.
     */
    private static final int DELAY = 10;

    /**
     * The parent which contains the startpanel.
     */
    private ISGCIMainFrame mainframe;

    /**
     * Creates a new StartPanel for ISGCI.
     * 
     * @param parent
     *            The reference to the window containing this panel.
     */
    public StartPanel(final ISGCIMainFrame parent) {
        
        mainframe = parent;
        
        // Buttons
        JButton drawButton = createDrawingTaskButton();
        JButton databaseButton = IconButtonFactory.createImageButton(
                IconButtonFactory.INFORMATION_ICON, 
                "Look at the boundary of a problem",
                "TODO");
        JButton settingsButton = IconButtonFactory.createImageButton(
                IconButtonFactory.INFORMATION_ICON, 
                "Determine common super and subclasses of two classes",
                "TODO");
        JButton aboutButton = IconButtonFactory.createImageButton(
                IconButtonFactory.INFORMATION_ICON, 
                "Determine the inclusion relation between two classes",
                "TODO");
        
        // set buttonsize
        final Dimension buttonSize = new Dimension(200, 50);
        
        drawButton.setPreferredSize(buttonSize);
        databaseButton.setPreferredSize(buttonSize);
        settingsButton.setPreferredSize(buttonSize);
        aboutButton.setPreferredSize(buttonSize);
        
        drawButton.setMinimumSize(buttonSize);
        databaseButton.setMinimumSize(buttonSize);
        settingsButton.setMinimumSize(buttonSize);
        aboutButton.setMinimumSize(buttonSize);
        
        // organize buttonlayout
        final int maxSize = 10000;
        
        JPanel leftPanel = new JPanel();
        BoxLayout layout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        
        leftPanel.setLayout(layout);
        
        leftPanel.add(new JLabel(new ImageIcon(
                getClass().getResource("/logo.png"))));
        leftPanel.add(drawButton);
        leftPanel.add(databaseButton);
        leftPanel.add(settingsButton);
        leftPanel.add(aboutButton);
        leftPanel.add(new Box.Filler(new Dimension(0, 0),
                                     buttonSize,
                                     new Dimension(maxSize, maxSize)));
      
        // set news-pane
        final Dimension newsSize = new Dimension(400, 100); 
        
        JComponent newsPane = loadNews();
        newsPane.setPreferredSize(newsSize);
        
        // join everything together
        final int gap = 5;
        setLayout(new BorderLayout(gap, gap));
        
        add(leftPanel, BorderLayout.LINE_START);
        add(newsPane, BorderLayout.CENTER);
       
    }
    
    /**
     * Creates a robot on the currently active screen.
     *  
     * @return
     *          A new robot on the currently active screen.
     */
    private Robot getRobot() {
        Robot robot = null;
        
        try {
            // first, try to create a robot
            robot = new Robot();
            
            // then, try to get a robot on the current screen, which is
            // more likely to throw an error
            robot = new Robot(mainframe.getGraphicsConfiguration()
                                .getDevice());
        } catch (Exception e) {
            System.err.println("Failed to initialize robot for moving mouse!");
        }
        
        return robot;
    }
    
    /**
     * Moves to mouse from the current position to a point by using a slightly
     * tweaked Bresenham's line algorithm.
     * 
     * @param robot
     *          The robot which is used to move the mouse.
     * 
     * @param to
     *          Where the mousepointer should move to.
     */
    private void moveMouseTo(Robot robot, Point to) {        
        Point start = MouseInfo.getPointerInfo().getLocation();
        
        boolean steep = Math.abs(to.y - start.y) > Math.abs(to.x - start.x);
        if (steep) {
            int t;
            
            // swap(start.x, start.y);
            t = start.x;
            start.x = start.y;
            start.y = t;
            
            // swap(to.x, to.y);
            t = to.x;
            to.x = to.y;
            to.y = t;
        }

        int deltax = Math.abs(to.x - start.x);
        int deltay = Math.abs(to.y - start.y);
        int error = deltax / 2;
        int ystep;
        int y = start.y;

        if (start.y < to.y) {
            ystep = 1;
        } else {
            ystep = -1;
        }

        for (int x = start.x; x != to.x;
                x += Math.signum(to.x - start.x) * 1) {

            if (steep) {
                robot.mouseMove(y, x);
            } else {
                robot.mouseMove(x, y);
            }

            error = error - deltay;

            if (error < 0) {
                y = y + ystep;
                error = error + deltax;
            }

            // delay animation so it's visible
            robot.delay(DELAY);
        }
    }
    
    /**
     * Creates a button, that - if clicked - will take over the mouse
     * and guide the user how to do a specific task, in this case:
     * xyz.
     * 
     * @return
     *          A jbutton that will guide the mouse upon click.
     */
    private JButton createDrawingTaskButton() {
        JButton button = IconButtonFactory.createImageButton(
                IconButtonFactory.ADD_ICON, "HOW TO: Draw a hierarchy",
                "Guides you through the process of drawing a hierarchy of "
                + "super- and/or subclasses of some class(es)."); 
        
        button.addActionListener(new ActionListener() {    
            @Override
            public void actionPerformed(ActionEvent e) {
                // create robot on the screen where the parent is active
                Robot mouseRobot = getRobot();
                
                if (mouseRobot == null) {
                    return;
                }
                
                // Click draw button
                JComponent endButton = mainframe.getToolbar().getDrawButton();
                Point endPoint = endButton.getLocationOnScreen();
                
                // add size/2 so button isn't clicked in left top corner
                endPoint.x += endButton.getWidth() / 2;
                endPoint.y += endButton.getHeight() / 2;
                
                moveMouseTo(mouseRobot, endPoint);
                
                // click
                mouseRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                mouseRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                
            }
        });
        
        return button;
    }
    
    /**
     * Loads news into a EditorPane.
     * @return
     *          The component containing the editorpane.
     */
    private JComponent loadNews() {
        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);

        try {
          jep.setPage(NEWSPAGE);
        } catch (IOException e) {
          jep.setContentType("text/html");
          jep.setText("<html>Could not load webpage</html>");
        } 

        JScrollPane scrollPane = new JScrollPane(jep);
        return scrollPane;
    }    
}

/* EOF */

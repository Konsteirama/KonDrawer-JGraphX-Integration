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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import teo.isgci.drawing.DrawingLibraryFactory;
import teo.isgci.drawing.DrawingLibraryInterface;
import teo.isgci.drawing.GraphManipulationInterface;
import teo.isgci.problem.Complexity;
import teo.isgci.util.UserSettings;

/**
 * A ISGCI specific implementation for the startpage of the tabbedpane that is
 * displayed once a new window is opened. It displays logo, most common
 * functions and news.
 */
class ISGCIStartPanel extends JPanel {

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
     * The drawinglibraryinterface used to draw the isgci graph.
     */
    private DrawingLibraryInterface<String, DefaultEdge> drawingLibInterface;
    
    /**
     * Creates a new StartPanel for ISGCI.
     * 
     * @param parent
     *          The reference to the window containing this panel.
     */
    public ISGCIStartPanel(final ISGCIMainFrame parent) {
        // Create a graphcanvas with ISGCI logo
        
        // space => make nodes bigger
        String i1 = "   I   "; 
        String s = "   S   ";
        String g = "   G   ";
        String c = "   C   ";
        String i2 = "   I    "; // needs to be one space bigger, else i1 == i2
        
        Graph<String, DefaultEdge> isgci 
            = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        
        isgci.addVertex(i1);
        isgci.addVertex(s);
        isgci.addVertex(g);
        isgci.addVertex(c);
        isgci.addVertex(i2);
        
        isgci.addEdge(i1, s);
        isgci.addEdge(s, c);
        isgci.addEdge(s, g);
        isgci.addEdge(g, c);
        isgci.addEdge(c, i2);
        
        drawingLibInterface = DrawingLibraryFactory.createNewInterface(isgci);
        
        // color nodes differently
        GraphManipulationInterface<String, DefaultEdge> gmi 
            = drawingLibInterface.getGraphManipulationInterface();
        
        gmi.colorNode(new String[] { i1 }, 
                UserSettings.getColor(Complexity.CONPC));
        gmi.colorNode(new String[] { s }, 
                UserSettings.getColor(Complexity.GIC));
        gmi.colorNode(new String[] { g }, 
                UserSettings.getColor(Complexity.LINEAR));
        gmi.colorNode(new String[] { c }, 
                UserSettings.getColor(Complexity.OPEN));
        gmi.colorNode(new String[] { i2 }, 
                UserSettings.getColor(Complexity.P));
        
        
        // create a boxlayout with buttons
        final int rows = 4;
        final int cols = 1;
        JPanel buttonPanel = new JPanel(new GridLayout(rows, cols));
        
        // Buttons
        JButton drawButton = new JButton("Create a new graph-drawing");
        JButton databaseButton = new JButton("Browse the graph database");
        JButton settingsButton = new JButton("Change settings");
        JButton aboutButton = new JButton("About ISGCI");
        
        // set buttonsize
        final Dimension buttonSize = new Dimension(200, 80);
        
        drawButton.setPreferredSize(buttonSize);
        databaseButton.setPreferredSize(buttonSize);
        settingsButton.setPreferredSize(buttonSize);
        aboutButton.setPreferredSize(buttonSize);
        
        // add actionhandler
        drawButton.addActionListener(new ActionListener() {    
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.openSelectGraphClassesDialog();
            }
        });
        
        databaseButton.addActionListener(new ActionListener() {    
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.openBrowseDatabaseDialog();
            }
        });
        
        settingsButton.addActionListener(new ActionListener() {    
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.openSettingsDialog();
            }
        });
        
        aboutButton.addActionListener(new ActionListener() {    
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.openAboutDialog();
            }
        });
        
        // add to layout
        buttonPanel.add(drawButton);
        buttonPanel.add(databaseButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(aboutButton);
        
        // set news-pane
        final Dimension newsSize = new Dimension(250, 300); 
        
        JComponent newsPane = loadNews();
        newsPane.setPreferredSize(newsSize);
        
        // join everything together
        final int gap = 5;
        setLayout(new BorderLayout(gap, gap));
        
        add(buttonPanel, BorderLayout.LINE_START);
        add(drawingLibInterface.getPanel(), BorderLayout.CENTER);
        add(newsPane, BorderLayout.LINE_END);
       
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
    
    /**
     * Retrieve the drawinglibraryinterface used to display the ISGCI graph.
     * @return
     *          The drawinglibraryinterface used by this class.
     */
    public DrawingLibraryInterface getDrawingLibraryInterface() {
        return drawingLibInterface;
    }
    
}

/* EOF */

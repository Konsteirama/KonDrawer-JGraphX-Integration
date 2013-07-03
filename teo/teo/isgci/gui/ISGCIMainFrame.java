/*
 * The main window of ISGCI. Also the class to start the program.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/ISGCIMainFrame.java,v 2.4 2013/04/07 10:51:04 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleDirectedGraph;
import teo.isgci.db.DataSet;
import teo.isgci.gc.ForbiddenClass;
import teo.isgci.gc.GraphClass;
import teo.isgci.grapht.GAlg;
import teo.isgci.grapht.Inclusion;
import teo.isgci.problem.Problem;
import teo.isgci.util.UserSettings;
import teo.isgci.xml.GraphMLWriter;

/*import teo.isgci.gc.GraphClass;
 import java.util.ArrayList;*/

/**
 * The main frame of the application.
 */
public class ISGCIMainFrame extends JFrame implements WindowListener {

    /**
     * The applicationname displayed in the titlebar.
     */
    public static final String APPLICATIONNAME = "ISGCI";
    
    /**
     * The location where new windows will be positioned.
     */
    private static final Point DEFAULTPOSITION = new Point(50, 50);
    
    /**
     * The location where new mainframes will be positioned.
     */
    private static final Point MAINPOSITION = new Point(20, 20);
    
    /**
     * This should change each time the mainframe is changed.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Needed for MediaTracker (hack).
     */
    public static ISGCIMainFrame tracker;
    // public static Font font;
    
    /**
     * The loader which initialized the mainframe.
     */
    protected teo.Loader loader;
    
    /**
     * Saves jdialogs to close them if they need to be closed from here.
     */
    private Vector<JDialog> dialogs = new Vector<JDialog>();
    
    /**
     * Needed so that menubar will not disappear on linux if maximized.
     */
    private JMenuBar mainMenuBar;
    
    /**
     * Needed for startpanel animation.
     */
    private JMenuItem miCheckInclusion, miOpenProblem, miGraphClassInformation,
            miSelectGraphClasses;
    
    /**
     * Indicates whether or not the graph should draw unproper edges.
     */
    private JMenuItem miDrawUnproper;
    /**
     * Needed for startpanel animation.
     */
    private JMenu graphMenu;
    /**
     * The tabbed Pane which handles all tabs and their creation / deletion.
     */
    private ISGCITabbedPane tabbedPane;
    /**
     * The toolbar, needed to for some startpanel animations.
     */
    private ISGCIToolBar toolbar;

    /**
     * Creates the frame.
     * <p/>
     * Old documentation: * param locationURL The path/URL to the
     * applet/application. * param isApplet true iff the program runs as an
     * applet.
     *
     * @param teoloader The loader which initializes this mainframe.
     */
    public ISGCIMainFrame(teo.Loader teoloader) {
        super(APPLICATIONNAME);

        teoloader.register();
        loader = teoloader;
        tracker = this;

        DataSet.init(teoloader, "data/isgci.xml");
        ForbiddenClass.initRules(teoloader, "data/smallgraphs.xml");
        PSGraphics.init(teoloader);
        LatexGraphics.init(teoloader);

        boolean createMaps = false;
        try {
            createMaps = System.getProperty("org.isgci.mappath") != null;
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        if (createMaps) { // Create maps and terminate
            new teo.isgci.util.LandMark(this).createMaps();
            closeWindow();
        }

        /*
         * { int sub = 0, equ = 0, incomp = 0, incompWit = 0, incompWitFin = 0;
         * 
         * ArrayList<ForbiddenClass> fcs = new ArrayList<ForbiddenClass>(); for
         * (GraphClass gc : DataSet.getClasses()) if (gc instanceof
         * ForbiddenClass) fcs.add((ForbiddenClass) gc);
         * 
         * for (int i = 0; i < fcs.size()-1; i++) { for (int j = i+1; j <
         * fcs.size(); j++) { boolean sub1 = fcs.get(i).subClassOf(fcs.get(j));
         * boolean sub2 = fcs.get(j).subClassOf(fcs.get(i)); if (sub1 && sub2)
         * equ++; else if (!sub1 && !sub2) { StringBuilder why1 = new
         * StringBuilder(); StringBuilder why2 = new StringBuilder(); Boolean
         * not1 = fcs.get(j).notSubClassOf( fcs.get(i), why1); Boolean not2 =
         * fcs.get(i).notSubClassOf( fcs.get(j), why2); if (not1 && not2) { if
         * (why1.length() > 0 && why2.length() > 0) { if (fcs.get(i).isFinite()
         * && fcs.get(j).isFinite()) incompWitFin++; else incompWit++; } else
         * incomp++; } } else sub++; } } System.out.println("Total: "+
         * fcs.size() + " sub: "+ sub + " equ: "+ equ + " incomparable: "+
         * incomp + " incomparable with finite witness: "+ incompWitFin +
         * " incomparable with witness: "+ incompWit); }
         */

        /*
         * writeGraphML(); closeWindow();
         */

        setJMenuBar(createMenus());

        // Create and add tabbed interface for canvas
        tabbedPane = new ISGCITabbedPane(this);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Create and add new toolbar - has to be after tabbedpane
        // because toolbar needs to listen to tabbedpane
        toolbar = new ISGCIToolBar(this);
        getContentPane().add(toolbar, BorderLayout.PAGE_START);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        // preferredsize is a bit too small, so increase it a bit
        Dimension prefSize = getPreferredSize();
        setMinimumSize(new Dimension(prefSize.width + 30,
                prefSize.height + 40));

        setLocation(MAINPOSITION);
        pack();
        setVisible(true);
    }

    /**
     * Closes all dialogs to bring the graphtab to front.
     */
    public void closeDialogs() {
        for (JDialog dialog : dialogs) {
            dialog.setVisible(false);
            dialog.dispose();
        }

        dialogs.clear();

        toFront();
    }

    /**
     * Closes the window and possibly terminates the program.
     */
    public void closeWindow() {
        UserSettings.unsubscribeFromOptionChanges(tabbedPane);
        setVisible(false);
        dispose();
        loader.unregister();
    }

    /**
     * Getter for the menu for drawing graphs.
     *
     * @return The menu for drawing graphs.
     */
    public JMenuItem getDrawItem() {
        return miSelectGraphClasses;
    }

    /**
     * Getter for the menu for the graph class selection menu.
     *
     * @return The menu for the graph class selection menu.
     */
    public JMenuItem getGraphClassInformationItem() {
        return miGraphClassInformation;
    }

    /**
     * Getter for the menu for the graph classes.
     *
     * @return The menu for the graph classes menu.
     */
    public JMenu getGraphMenu() {
        return graphMenu;
    }

    /**
     * Getter for the menuitem of search for inclusion.
     *
     * @return The menuitem for inclusions.
     */
    public JMenuItem getInclusionMenuItem() {
        return miCheckInclusion;
    }

    /**
     * Getter for the menu for the open problem menu.
     *
     * @return The menu for the open problem menu.
     */
    public JMenuItem getOpenProblemMenuItem() {
        return miOpenProblem;
    }

    /**
     * Getter for {@link #tabbedPane}.
     *
     * @return Returns {@link #tabbedPane}.
     */
    public ISGCITabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Getter for {@link #toolbar}.
     *
     * @return Returns {@link #toolbar}.
     */
    public ISGCIToolBar getToolbar() {
        return toolbar;
    }

    /**
     * Opens the about dialog.
     */
    public void openAboutDialog() {
        JDialog about = new AboutDialog(this);
        about.setLocation(DEFAULTPOSITION);
        about.setVisible(true);

        dialogs.add(about);
    }

    /**
     * Opens the dialog to browse the database.
     */
    public void openBrowseDatabaseDialog() {
        final int width = 800;
        final int height = 600;

        JDialog info = new GraphClassInformationDialog(this);
        info.setLocation(DEFAULTPOSITION);
        info.pack();
        info.setSize(width, height);
        info.setVisible(true);

        dialogs.add(info);
    }

    /**
     * Opens a new export dialog.
     */
    public void openExportDialog() {
        if (getTabbedPane().getActiveDrawingLibraryInterface() == null) {
            MessageDialog.error(this,
                    "The active tab contains no graph to export.");
            return;
        }

        JDialog export = new ExportDialog(this);
        export.setLocation(DEFAULTPOSITION);
        export.pack();
        export.setVisible(true);

        dialogs.add(export);
    }

    /**
     * Opens the search in drawing dialog.
     */
    public void openSearchDialog() {
        if (getTabbedPane().getActiveDrawingLibraryInterface() == null) {
            MessageDialog.error(this,
                    "The active tab contains no graph to search in.");
            return;
        }

        JDialog search = new SearchDialog(this);
        search.setLocation(DEFAULTPOSITION);
        search.setVisible(true);

        dialogs.add(search);
    }

    /**
     * Opens a new SelectGraphClasses dialog.
     */
    public void openSelectGraphClassesDialog() {
        final int width = 500;
        final int height = 400;

        JDialog select = new GraphClassSelectionDialog(this);
        select.setLocation(DEFAULTPOSITION);
        select.pack();
        select.setSize(width, height);
        select.setVisible(true);

        dialogs.add(select);
    }

    /**
     * Opens the settings dialog.
     */
    public void openSettingsDialog() {
        JDialog settings = new SettingsDialog(this);
        settings.setLocation(DEFAULTPOSITION);
        settings.setVisible(true);

        dialogs.add(settings);
    }

    /**
     * Sets the state of the color for problem radiobox.
     *
     * @param problem the new problem which is active.
     */
    public void setColorProblem(Problem problem) {
        String name;
        if (problem == null) {
            name = "None";
        } else {
            name = problem.getName();
        }

        toolbar.setProblem(name);
    }

    /**
     * Sets whether the Draw Unproper checkbox is checked.
     *
     * @param state True if the checkbox should be checked, false if not.
     */
    public void setDrawUnproper(boolean state) {
        miDrawUnproper.setSelected(state);
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        closeWindow();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    /**
     * Adds View to the menu.
     */
    private void addFileMenu() {

        JMenuItem miNew = new JMenuItem("New window");
        miNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ISGCIMainFrame(loader);
            }
        });

        JMenuItem miExport = new JMenuItem("Export drawing...");
        miExport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openExportDialog();
            }
        });

        JMenuItem miExit = new JMenuItem("Exit");
        miExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(miNew);
        fileMenu.add(miExport);
        fileMenu.add(miExit);
        mainMenuBar.add(fileMenu);
    }

    /**
     * Adds Help to the menu.
     */
    private void addGraphMenu() {
        // needed to reference this in actionlistener
        final ISGCIMainFrame mainframe = this;

        miGraphClassInformation = new JMenuItem("Browse Database");
        miGraphClassInformation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openBrowseDatabaseDialog();
            }
        });

        miOpenProblem = new JMenu("Boundary/Open classes");
        for (int i = 0; i < DataSet.problems.size(); i++) {
            JMenuItem menu = new JMenuItem(
                    ((Problem) DataSet.problems.elementAt(i)).getName());
            miOpenProblem.add(menu);
            menu.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog open = new OpenProblemDialog(mainframe,
                            ((JMenuItem) e.getSource()).getText());
                    open.setLocation(DEFAULTPOSITION);
                    open.setVisible(true);
                }
            });
        }
        
        miCheckInclusion = new JMenuItem("Find Relation...");
        miCheckInclusion.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final int width = 700;
                final int height = 400;

                JDialog check = new CheckInclusionDialog(mainframe);
                check.setLocation(DEFAULTPOSITION);
                check.pack();
                check.setSize(width, height);
                check.setVisible(true);

                dialogs.add(check);
            }
        });
        
        miSelectGraphClasses = new JMenuItem("Draw...");
        miSelectGraphClasses.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openSelectGraphClassesDialog();
            }
        });

        graphMenu = new JMenu("Graph classes");
        graphMenu.add(miGraphClassInformation);
        graphMenu.add(miOpenProblem);
        graphMenu.add(miCheckInclusion);
        graphMenu.add(miSelectGraphClasses);
        mainMenuBar.add(graphMenu);
    }

    /**
     * Adds Help to the menu.
     */
    private void addHelpMenu() {

        JMenuItem miSmallgraphs = new JMenuItem("Small graphs");
        miSmallgraphs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loader.showDocument("smallgraphs.html");
            }
        });

        JMenuItem miHelp = new JMenuItem("Help");
        miHelp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loader.showDocument("help.html");
            }
        });

        JMenuItem miAbout = new JMenuItem("About");
        miAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openAboutDialog();
            }
        });


        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(miSmallgraphs);
        helpMenu.add(miHelp);
        helpMenu.add(miAbout);

        // mainMenuBar.add(Box.createHorizontalGlue());
        mainMenuBar.add(helpMenu);
    }

    /**
     * Adds View to the menu.
     */
    private void addViewMenu() {

        // needed to reference this in actionlistener
        final ISGCIMainFrame mainframe = this;

        JMenuItem miSearching = new JMenuItem("Search in drawing...");
        miSearching.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchDialog();
            }
        });

        JMenuItem miNaming = new JMenuItem("Naming preference...");
        miNaming.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog naming = new NamingDialog(mainframe);
                naming.setLocation(DEFAULTPOSITION);
                naming.pack();
                naming.setVisible(true);

                dialogs.add(naming);
            }
        });

        miDrawUnproper = new JCheckBoxMenuItem("Mark unproper inclusions",
                true);
        miDrawUnproper.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Object object = e.getSource();
                if (object == miDrawUnproper) {
                    getTabbedPane().setDrawUnproper(
                            ((JCheckBoxMenuItem) object).getState(),
                            getTabbedPane().getSelectedComponent());
                }
            }
        });

        JMenuItem miColorSettings = new JMenuItem("Set colours...");
        miColorSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openSettingsDialog();
            }
        });

        JMenu viewMenu = new JMenu("View");
        viewMenu.add(miSearching);
        viewMenu.add(miNaming);
        viewMenu.add(miDrawUnproper);
        viewMenu.add(miColorSettings);

        mainMenuBar.add(viewMenu);
    }

    /**
     * Write the entire database in GraphML to isgcifull.graphml.
     */
    private void writeGraphML() {
        OutputStreamWriter out = null;

        SimpleDirectedGraph<GraphClass, Inclusion> g
            = new SimpleDirectedGraph<GraphClass, Inclusion>(Inclusion.class);
        Graphs.addGraph(g, DataSet.inclGraph);
        GAlg.transitiveReductionBruteForce(g);

        try {
            out = new OutputStreamWriter(new FileOutputStream(
                    "isgcifull.graphml"), "UTF-8");
            GraphMLWriter w = new GraphMLWriter(out, GraphMLWriter.MODE_PLAIN,
                    true, false);
            w.startDocument();
            for (GraphClass gc : g.vertexSet()) {
                w.writeNode(gc.getID(), gc.toString(), Color.WHITE);
            }
            for (Inclusion e : g.edgeSet()) {
                w.writeEdge(e.getSuper().getID(), e.getSub().getID(),
                        e.isProper());
            }
            w.endDocument();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the menu system.
     *
     * @return The created JMenuBar
     * @see JMenuBar
     */
    protected JMenuBar createMenus() {
        mainMenuBar = new JMenuBar();

        addFileMenu();
        addViewMenu();
        addGraphMenu();
        addHelpMenu();

        return mainMenuBar;
    }
}

/* EOF */

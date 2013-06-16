/*
 * A dialog where the user can change various settings.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import teo.isgci.db.Algo;
import teo.isgci.problem.Complexity;
import teo.isgci.util.UserSettings;

/**
 * A dialog where the user can change various settings.
 * 
 */
public class ISGCISettingsDialog extends JDialog implements ActionListener,
        ListSelectionListener, ChangeListener {

    /** TODO marc JAVADOCS. */
    private JTabbedPane options;
    
    /** TODO marc JAVADOCS. */
    private JButton applyButton, uiSetDefaultButton;
    
    /** TODO marc JAVADOCS. */
    private JButton gcSetDefaultButton, gcColorBlind;
    
    /** TODO marc JAVADOCS. */
    private JList colourOptions;
    
    /** TODO marc JAVADOCS. */
    private JColorChooser colours;
    
    /** TODO marc JAVADOCS. */
    private JComboBox tabOrientation, theme, namingPreference; // nachschlagen
    
    /** TODO marc JAVADOCS. */
    private String[] tabs = new String[] { "Top", "East", "West", "Bottom" };
    
    /** TODO marc JAVADOCS. */
    private String[] themes = new String[] { "Metal", "Nimbus",
            "Platform standard" };
    
    /** TODO marc JAVADOCS. */
    private String[] problemes = new String[] { "font colour",
            "backgroundcolour", "unkown complexity",
            "GraphIsomorphism-complete", "NP-complete", "lineartime solvable",
            "P", "open classes", "CONPC", "NPH", "keine" };
    
    /** TODO marc JAVADOCS. */
    private String[] naming = new String[] { "Basic", "Forbidden", "Derived" };
    
    /** TODO marc JAVADOCS. */
    private JCheckBox toolbar, groupColours;
    
    /** TODO marc JAVADOCS. */
    private JSlider zoomLevel;
    
    /** TODO marc JAVADOCS. */
    private Color unknown, gic, npc, linear, p, open, conpc, nph, empty, text,
                  background;
    
    /** TODO marc JAVADOCS. */
    private HashMap<Complexity, Color> colorscheme 
        = new HashMap<Complexity, Color>();
    
    /** TODO marc JAVADOCS. */
    private Boolean group, tb;
    
    /** TODO marc JAVADOCS. */
    private int zoom;
    
    /** TODO marc JAVADOCS. */
    private int tabPlacement;

    /** TODO marc JAVADOCS. */
    private String javaTheme;
    
    /** TODO marc JAVADOCS. */
    private Algo.NamePref namepref;
    
    /** TODO marc JAVADOCS. */
    private Boolean setToolbar, setTheme, setZoom, setTabOrientation, 
                    setNamingPreferences;

    /**
     * This field should change every time the class is changed - once it is
     * actually deployed.
     */
    private static final long serialVersionUID = 2L;

    /**
     * Creates a new options dialogue.
     * @param parent
     *          The mainframe from which the dialog is created.
     */
    public ISGCISettingsDialog(ISGCIMainFrame parent) {
        super(parent, "Settings", true);

        // group
        zoom = UserSettings.getCurrentZoomLevel();
        options = new JTabbedPane(UserSettings.getCurrentTabPlacement());
        tabPlacement = UserSettings.getCurrentTabPlacement();
        unknown = UserSettings.getColor(Complexity.UNKNOWN);
        gic = UserSettings.getColor(Complexity.GIC);
        npc = UserSettings.getColor(Complexity.NPC);
        linear = UserSettings.getColor(Complexity.LINEAR);
        p = UserSettings.getColor(Complexity.P);
        open = UserSettings.getColor(Complexity.OPEN);
        conpc = UserSettings.getColor(Complexity.CONPC);
        nph = UserSettings.getColor(Complexity.NPH);
        empty = UserSettings.getColor(null);

        colours = new JColorChooser();

        createLayout();

        /*
         * Creates the tabs and the integrated panels.
         */
        options.addTab("User Interface", createUserInterfaceTab());
        options.addTab("Graph Colours", createColorTab());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        /*
         * Add action listeners to all components.
         */
        uiSetDefaultButton.addActionListener(this);
        gcSetDefaultButton.addActionListener(this);
        gcColorBlind.addActionListener(this);
        groupColours.addActionListener(this);
        tabOrientation.addActionListener(this);
        theme.addActionListener(this);
        toolbar.addActionListener(this);
        colourOptions.addListSelectionListener(this);
        colours.getSelectionModel().addChangeListener(this);
        namingPreference.addActionListener(this);
        zoomLevel.addChangeListener(this);
    }

    /**
     * Creates a panel where the user can change settings related to the
     * UserInterface.
     * @return
     *          Panel with various sliders and boxes.
     */
    private JPanel createUserInterfaceTab() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel tabUserInterface = new JPanel(gridbag);
        JPanel userInterface = new JPanel(gridbag);
        
        // CONSTANTS
        final int gap = 5;
        final int bigGap = 10;
        final int width = 30;
        final int height = 10;
        
        /*
         * Adds the user interface panel to the user interface tab.
         */
        Border ui = new BevelBorder(BevelBorder.RAISED);
        userInterface.setBorder(ui);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(gap, gap, bigGap, gap);
        c.weightx = 1.0;
        final double weighty = 0.85;
        c.weighty = weighty;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabUserInterface.add(userInterface, c);

        /*
         * Adds a description and a slider to the user interface panel to set
         * the standard zoom level.
         */
        final int majorSpacing = 10;
        
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        JLabel label = new JLabel("Set default zoom level", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        zoomLevel = new JSlider(JSlider.HORIZONTAL, -bigGap, bigGap, 0);
        zoomLevel.setMajorTickSpacing(majorSpacing);
        zoomLevel.setMinorTickSpacing(1);
        zoomLevel.setPaintTicks(true);
        userInterface.add(zoomLevel, c);

        /*
         * Adds a description and a checkbox to the user interface panel to 
         * show or hide the toolbar.
         */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Display Toolbar", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        toolbar = new JCheckBox();
        toolbar.setSelected(true);
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        userInterface.add(toolbar, c);

        /*
         * Adds a description and a combobox to the user interface panel to
         * choose the placement of the tab line.
         */
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Tab Orientation", JLabel.LEFT);
        userInterface.add(label, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabOrientation = new JComboBox(tabs);
        if (tabPlacement == JTabbedPane.TOP) {
            tabOrientation.setSelectedIndex(0);
        } else if (tabPlacement == JTabbedPane.RIGHT) {
            tabOrientation.setSelectedIndex(1);
        } else if (tabPlacement == JTabbedPane.LEFT) {
            tabOrientation.setSelectedIndex(2);
        } else if (tabPlacement == JTabbedPane.BOTTOM) {
            tabOrientation.setSelectedIndex(3);
        }
        tabOrientation.setSize(width, height);
        userInterface.add(tabOrientation, c);

        /*
         * Adds a description and a combobox to the user interface panel to
         * choose the java theme for all windows.
         */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Set Java Theme", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        theme = new JComboBox(themes);
        theme.setSelectedIndex(0);
        theme.setSize(width, height);
        userInterface.add(theme, c);

        /*
         * Adds a description and a combobox to the user interface panel to
         * choose the naming preference.
         */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Set naming preference", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        namingPreference = new JComboBox(naming);
        namingPreference.setSelectedIndex(0);
        namingPreference.setSize(width, height);
        userInterface.add(namingPreference, c);
        
        return userInterface;
    }
    
    
    /**
     * Creates a panel where the user can change colors.
     * @return
     *          Panel with colorchooser and list.
     */
    private JPanel createColorTab() {
        JPanel graphColours = new JPanel(new GridBagLayout());
        JPanel tabGraphColours = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        // CONSTANTS
        final int gap = 5;
        final int bigGap = 20;
        final int hugeGap = 40;
        
        /*
         * Adds the graph colours panel to the graph colours tab.
         */
        Border graph = new BevelBorder(BevelBorder.RAISED);
        graphColours.setBorder(graph);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(gap, gap, bigGap, gap);
        c.weightx = 1.0;
        c.weighty = 0.85;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabGraphColours.add(graphColours, c);

        /*
         * Adds a list, a colour chooser and a checkbox to the graph colours
         * panel. The list holds the different problems, for which the user can
         * colour the graph. With the colour chooser the user can set colours
         * for the options in the list. If the checkbox is activated, the
         * default grouping of problem colours is set.
         */
        c.gridwidth = GridBagConstraints.RELATIVE;
        colourOptions = new JList(problemes);
        colourOptions.setSelectedIndex(1);
        graphColours.add(colourOptions, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(bigGap, gap, bigGap, gap);
        graphColours.add(colours, c);
        c.anchor = GridBagConstraints.WEST;
        groupColours = new JCheckBox("Group problems");
        groupColours.setSelected(true);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        graphColours.add(groupColours, c);

        /*
         * Adds a button to the graph colours tab to get back the default 
         * coloursetting.
         */
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, gap, bigGap, hugeGap);
        c.gridwidth = 1;
        gcSetDefaultButton = new JButton("Default Colours");
        tabGraphColours.add(gcSetDefaultButton, c);

        /*
         * Adds a button to the graph colours tab to get the colour blind 
         * colour setting.
         */
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, gap, bigGap, gap);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gcColorBlind = new JButton("Set scheme for color blind");
        tabGraphColours.add(gcColorBlind, c);
        
        return tabGraphColours;
    }
    
    /**
     * Creates the basic layout with tabbedpane and apply etc buttons.
     */
    private void createLayout() {
        setLayout(new BorderLayout());
        add(options, BorderLayout.CENTER);

        // bottomleft
        // to make the button as small as the right buttons
        JPanel bottomLeftPanel = new JPanel(new FlowLayout());

        uiSetDefaultButton = new JButton("Default settings");
        bottomLeftPanel.add(uiSetDefaultButton, BorderLayout.LINE_START);

        // bottomright
        JPanel bottomRightPanel = new JPanel(new FlowLayout());

        // OK Button
        JButton okButton = new JButton("Ok");
        bottomRightPanel.add(okButton);

        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Saves all changes and close the dialogue.
                if (applyButton.isEnabled()) {
                    applyButton.doClick();
                }
                closeDialog();
            }
        });
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        bottomRightPanel.add(cancelButton);
        
        cancelButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        // Apply button
        applyButton = new JButton("Apply");
        applyButton.setEnabled(false);
        bottomRightPanel.add(applyButton);
        
        applyButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                // Saves all changes.
                applyButton.setEnabled(false);
                if (setNamingPreferences) {
                    UserSettings.setNamingPref(namepref);
                }
                if (setTabOrientation) {
                    UserSettings.setTabPlacement(tabPlacement);
                }
                if (setTheme) {
                    UserSettings.setTheme(javaTheme);
                }
                if (setZoom) {
                    UserSettings.setZoomLevel(zoom);
                }
                if (setToolbar) {
                    UserSettings.setToolbarSetting(toolbar.isSelected());
                }
                if (!colorscheme.isEmpty()) {
                    UserSettings.setColorScheme(colorscheme);
                    colorscheme.clear();
                }
            }
        });

        // merge bottom and add to general layout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomLeftPanel, BorderLayout.LINE_START);
        bottomPanel.add(bottomRightPanel, BorderLayout.LINE_END);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
    
    /**
     * Defines the action a button, combobox or checkbox executes when pressed.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

                /*
                 * Set all user interface options to default.
                 */
                if (source == uiSetDefaultButton) {
                    zoom = UserSettings.getDefaultZoomLevel();
                    zoomLevel.setValue(zoom);
                    tabOrientation.setSelectedIndex(0);
                    namingPreference.setSelectedIndex(0);
                    // group
                    theme.setSelectedIndex(0);
                    toolbar.setSelected(true);
                }

        /*
         * Set colour blind colour scheme.
         */
        if (source == gcColorBlind) {
            colorscheme = UserSettings.getDefaultColorBlindColorScheme();
            for (Complexity complexity : colorscheme.keySet()) {
                if (complexity == Complexity.UNKNOWN) {
                    unknown = colorscheme.get(complexity);
                } else if (complexity == Complexity.CONPC) {
                    conpc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPC) {
                    npc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPH) {
                    nph = colorscheme.get(complexity);
                } else if (complexity == null) {
                    empty = colorscheme.get(complexity);
                } else if (complexity == Complexity.P) {
                    p = colorscheme.get(complexity);
                } else if (complexity == Complexity.GIC) {
                    gic = colorscheme.get(complexity);
                } else if (complexity == Complexity.LINEAR) {
                    linear = colorscheme.get(complexity);
                } else if (complexity == Complexity.OPEN) {
                    open = colorscheme.get(complexity);
                }
            }
            int i = colourOptions.getSelectedIndex();
            colourOptions.clearSelection();
            colourOptions.setSelectedIndex(i);
        }
        /*
         * Set default colour scheme.
         */
        else if (source == gcSetDefaultButton) {
            colorscheme = UserSettings.getDefaultColorScheme();
            for (Complexity complexity : colorscheme.keySet()) {
                if (complexity == Complexity.UNKNOWN) {
                    unknown = colorscheme.get(complexity);
                } else if (complexity == Complexity.CONPC) {
                    conpc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPC) {
                    npc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPH) {
                    nph = colorscheme.get(complexity);
                } else if (complexity == null) {
                    empty = colorscheme.get(complexity);
                } else if (complexity == Complexity.P) {
                    p = colorscheme.get(complexity);
                } else if (complexity == Complexity.GIC) {
                    gic = colorscheme.get(complexity);
                } else if (complexity == Complexity.LINEAR) {
                    linear = colorscheme.get(complexity);
                } else if (complexity == Complexity.OPEN) {
                    open = colorscheme.get(complexity);
                }
            }
            groupColours.setSelected(true);
            int i = colourOptions.getSelectedIndex();
            colourOptions.clearSelection();
            colourOptions.setSelectedIndex(i);
        }
        /*
         * Set choosen tab placement.
         */
        else if (source == tabOrientation) {
            int x = tabOrientation.getSelectedIndex();
            if (x == 0) {
                tabPlacement = JTabbedPane.TOP;
                options.setTabPlacement(JTabbedPane.TOP);
            } else if (x == 1) {
                tabPlacement = JTabbedPane.RIGHT;
                options.setTabPlacement(JTabbedPane.RIGHT);
            } else if (x == 2) {
                tabPlacement = JTabbedPane.LEFT;
                options.setTabPlacement(JTabbedPane.LEFT);
            } else if (x == 3) {
                tabPlacement = JTabbedPane.BOTTOM;
                options.setTabPlacement(JTabbedPane.BOTTOM);
            }
            setTabOrientation = true;
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        }
        /*
         * Set choosen java theme.
         */
        else if (source == theme) {
            int i = theme.getSelectedIndex();
            if (i == 0) {
                try {
                    javaTheme = UIManager
                            .getCrossPlatformLookAndFeelClassName();
                    UIManager.setLookAndFeel(UIManager
                            .getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(this);
                this.pack();
            } else if (i == 1) {
                for (LookAndFeelInfo info : UIManager
                        .getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        try {
                            javaTheme = info.getName();
                            UIManager.setLookAndFeel(info.getClassName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        break;
                    }
                }
                SwingUtilities.updateComponentTreeUI(this);
                pack();
            } else if (i == 2) {
                try {
                    javaTheme = UIManager.getSystemLookAndFeelClassName();
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                } 
                
                SwingUtilities.updateComponentTreeUI(this);
                this.pack();
            }
            setTheme = true;
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        }
        /*
         * Enables or disables the toolbar.
         */
        else if (source == toolbar) {
            toolbar.setSelected(toolbar.isSelected());
            setToolbar = (!setToolbar);
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        }
        /*
         * Enables the default grouping of the problems.
         */
        else if (source == groupColours) {
            group = groupColours.isSelected();
        }
        /*
         * Set the naming preference.
         */
        else if (source == namingPreference) {
            int i = namingPreference.getSelectedIndex();
            if (i == 0) {
                namepref = Algo.NamePref.BASIC;
            } else if (i == 1) {
                namepref = Algo.NamePref.FORBIDDEN;
            } else if (i == 2) {
                namepref = Algo.NamePref.DERIVED;
            }
            if (namepref != UserSettings.getNamingPref()) {
                setNamingPreferences = true;
                if (!applyButton.isEnabled()) {
                    applyButton.setEnabled(true);
                }
            }
        }
    }

    /**
     * Close and dispose the dialogue.
     */
    public void closeDialog() {
        setVisible(false);
        dispose();
    }

    /**
     * Changes the colour chooser to the colour of the corresponding selection
     * choosen in the list.
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {
        int i = colourOptions.getSelectedIndex();
        if (i == 0) {
            colours.setColor(text);
        } else if (i == 1) {
            colours.setColor(background);
        } else if (i == 2) {
            colours.setColor(unknown);
        } else if (i == 3) {
            colours.setColor(gic);
        } else if (i == 4) {
            colours.setColor(npc);
        } else if (i == 5) {
            colours.setColor(linear);
        } else if (i == 6) {
            colours.setColor(p);
        } else if (i == 7) {
            colours.setColor(open);
        } else if (i == 8) {
            colours.setColor(conpc);
        } else if (i == 9) {
            colours.setColor(nph);
        } else if (i == 10) {
            colours.setColor(empty);
        }
    }

    /**
     * Defines the action the colour chooser and slider does when a new colour
     * is choosen or the slider is moved.
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        Object source = event.getSource();
        if (source == colours) {
            Color newColor = colours.getColor();
            int s = colourOptions.getSelectedIndex();
            if (s == 0) {
                if (newColor != text) {
                    text = newColor;
                    applyButton.setEnabled(true);
                }
            } else if (s == 1) {
                if (newColor != background) {
                    background = newColor;
                    applyButton.setEnabled(true);
                }
            } else if (s == 2) {
                if (newColor != unknown) {
                    unknown = newColor;
                    colorscheme.remove(unknown);
                    colorscheme.put(Complexity.UNKNOWN, unknown);
                    applyButton.setEnabled(true);
                }
                if (group) {
                    empty = newColor;
                    open = newColor;
                }
            } else if (s == 3) {
                if (newColor != gic) {
                    gic = newColor;
                    colorscheme.remove(gic);
                    colorscheme.put(Complexity.GIC, gic);
                    applyButton.setEnabled(true);
                }
            } else if (s == 4) {
                if (newColor != npc) {
                    npc = newColor;
                    colorscheme.remove(npc);
                    colorscheme.put(Complexity.NPC, npc);
                    applyButton.setEnabled(true);
                }
                if (group) {
                    nph = newColor;
                    conpc = newColor;
                }
            } else if (s == 5) {
                if (newColor != linear) {
                    linear = newColor;
                    colorscheme.remove(linear);
                    colorscheme.put(Complexity.LINEAR, linear);
                    applyButton.setEnabled(true);
                }
            } else if (s == 6) {
                if (newColor != p) {
                    p = newColor;
                    colorscheme.remove(p);
                    colorscheme.put(Complexity.P, p);
                    applyButton.setEnabled(true);
                }
            } else if (s == 7) {
                if (newColor != open) {
                    open = newColor;
                    colorscheme.remove(open);
                    colorscheme.put(Complexity.OPEN, open);
                    applyButton.setEnabled(true);
                }
                if (group) {
                    unknown = newColor;
                    empty = newColor;
                }
            } else if (s == 8) {
                if (newColor != conpc) {
                    conpc = newColor;
                    colorscheme.remove(conpc);
                    colorscheme.put(Complexity.CONPC, conpc);
                    applyButton.setEnabled(true);
                }
                if (group) {
                    nph = newColor;
                    npc = newColor;
                }
            } else if (s == 9) {
                if (newColor != nph) {
                    nph = newColor;
                    colorscheme.remove(nph);
                    colorscheme.put(Complexity.NPH, nph);
                    applyButton.setEnabled(true);
                }
                if (group) {
                    npc = newColor;
                    conpc = newColor;
                }
            } else if (s == 10) {
                if (newColor != empty) {
                    empty = newColor;
                    colorscheme.remove(empty);
                    colorscheme.put(null, empty);
                    applyButton.setEnabled(true);
                }
                if (group) {
                    unknown = newColor;
                    open = newColor;
                }
            }
        } else if (source == zoomLevel) {
            if (!zoomLevel.getValueIsAdjusting()) {
                int zoomlvl = (int) zoomLevel.getValue();
                zoom = zoomlvl;
            }
        }
    }
}
/* EOF */

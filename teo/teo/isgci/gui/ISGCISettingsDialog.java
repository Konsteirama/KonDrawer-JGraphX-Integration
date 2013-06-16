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
import java.util.Vector;

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
import teo.isgci.db.Algo.NamePref;
import teo.isgci.problem.Complexity;
import teo.isgci.util.UserSettings;

/**
 * A dialog where the user can change various settings.
 * 
 */
public class ISGCISettingsDialog extends JDialog {

    /** TODO marc JAVADOCS. */
    private JTabbedPane tabContainer;
    
    /** TODO marc JAVADOCS. */
    private JButton applyButton;
    
    /** TODO marc JAVADOCS. */
    private JList colorOptionsList;
    
    /** TODO marc JAVADOCS. */
    private JComboBox tabOrientation, themeSelector, 
                      namingPreference; // nachschlagen
    
    /** Needs to stay in that order, because
     *  TOP == 1
     *  LEFT == 2
     *  BOTTOM == 3
     *  RIGHT == 4. 
     */
    private String[] tabs = new String[] { "Top", "Left", "Bottom", "Right" };
    
    /** TODO marc JAVADOCS. */
    private Vector<String> availableThemes;
    
    /** TODO marc JAVADOCS. */
    private String[] problems = new String[] { "Font",
            "Background", "Unkown complexity",
            "Graph-isomorphism-complete", "NP-complete", "Lineartime solvable",
            "P", "Open classes", "CONPC", "NPH", "none" };
    
    /** TODO marc JAVADOCS. */
    private NamePref[] naming;
    
    /** TODO marc JAVADOCS. */
    private JCheckBox toolbar, groupcolors;
    
    /** TODO marc JAVADOCS. */
    private JSlider zoomLevel;
    
    /** TODO marc JAVADOCS. */
    private Color unknown, gic, npc, linear, p, open, conpc, nph, empty, text,
                  background;
    
    /** TODO marc JAVADOCS. */
    private HashMap<Complexity, Color> colorscheme 
        = new HashMap<Complexity, Color>();
    
    /** TODO marc JAVADOCS. */
    private Boolean group;
    
    /** TODO marc JAVADOCS. */
    private int zoom;
    
    /** TODO marc JAVADOCS. */
    private int tabPlacement;

    /** TODO marc JAVADOCS. */
    private String javaTheme;
    
    /** TODO marc JAVADOCS. */
    private Algo.NamePref namepref;

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

        // get available themes
        availableThemes = new Vector<String>();
        
        UIManager.LookAndFeelInfo[] lookAndFeelInfos 
            = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : lookAndFeelInfos) {
            availableThemes.add(lookAndFeelInfo.getName());
        }

        // populate naming
        naming = Algo.NamePref.values();
        
        // group
        zoom = UserSettings.getCurrentZoomLevel();
        tabContainer = new JTabbedPane(UserSettings.getCurrentTabPlacement());
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

        createLayout();

        /*
         * Creates the tabs and the integrated panels.
         */
        tabContainer.addTab("User Interface", createUserInterfaceTab());
        tabContainer.addTab("Graph colors", createColorTab());

        pack();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

        zoomLevel.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                
                if (!zoomLevel.getValueIsAdjusting()) {
                    int zoomlvl = (int) zoomLevel.getValue();
                    zoom = zoomlvl;
                    enableApplyButton();
                }
            }
        });
        
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
        
        toolbar.addActionListener(new ActionListener() {
            
            /*
             * Enables or disables the toolbar 
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbar.setSelected(toolbar.isSelected());
                enableApplyButton();
            }
        });

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
        
        tabOrientation.setSelectedIndex(tabPlacement - 1);

        tabOrientation.setSize(width, height);
        userInterface.add(tabOrientation, c);
        
        tabOrientation.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                tabPlacement = tabOrientation.getSelectedIndex() + 1;
                enableApplyButton();
            }
        });

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
        themeSelector = new JComboBox(availableThemes);
        themeSelector.setSelectedIndex(0);
        themeSelector.setSize(width, height);
        userInterface.add(themeSelector, c);
        
        // needed for reference in actionlistener
        final ISGCISettingsDialog dialog = this;
        
        themeSelector.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager
                        .getInstalledLookAndFeels();
                
                // search for matching look and feel
                for (UIManager.LookAndFeelInfo info : lookAndFeelInfos) {
                    if (info.getName().equals(
                            themeSelector.getSelectedItem())) {
                        javaTheme = info.getClassName();
                        // found the correct one, no need to search further
                        break;
                    }
                }
                
                if (!javaTheme.equals(UserSettings.getCurrentTheme())) {
                    enableApplyButton();
                } 
                
            }
        });

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
        
        namingPreference.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                namepref = (Algo.NamePref) namingPreference.getSelectedItem();

                if (namepref != UserSettings.getNamingPref()) {
                    enableApplyButton();
                }
            }
        });
        
        return userInterface;
    }
    
    
    /**
     * Creates a panel where the user can change colors.
     * @return
     *          Panel with colorchooser and list.
     */
    private JPanel createColorTab() {
        JPanel graphcolors = new JPanel(new GridBagLayout());
        JPanel tabGraphcolors = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        // CONSTANTS
        final int gap = 5;
        final int bigGap = 20;
        final int hugeGap = 40;
        
        /*
         * Adds the graph colors panel to the graph colors tab.
         */
        Border graph = new BevelBorder(BevelBorder.RAISED);
        graphcolors.setBorder(graph);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(gap, gap, bigGap, gap);
        c.weightx = 1.0;
        c.weighty = 0.85;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabGraphcolors.add(graphcolors, c);

        /*
         * Adds a list, a color chooser and a checkbox to the graph colors
         * panel. The list holds the different problems, for which the user can
         * color the graph. With the color chooser the user can set colors
         * for the options in the list. If the checkbox is activated, the
         * default grouping of problem colors is set.
         */
        
        final JColorChooser colorChooser = new JColorChooser();
        c.gridwidth = GridBagConstraints.RELATIVE;
        colorOptionsList = new JList(problems);
        colorOptionsList.setSelectedIndex(1);
        graphcolors.add(colorOptionsList, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(bigGap, gap, bigGap, gap);
        graphcolors.add(colorChooser, c);
        c.anchor = GridBagConstraints.WEST;
        groupcolors = new JCheckBox("Group problems");
        groupcolors.setSelected(true);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        graphcolors.add(groupcolors, c);

        colorChooser.getSelectionModel().addChangeListener(
                new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Color newColor = colorChooser.getColor();
                int s = colorOptionsList.getSelectedIndex();
                if (s == 0) {
                    if (newColor != text) {
                        text = newColor;
                        enableApplyButton();
                    }
                } else if (s == 1) {
                    if (newColor != background) {
                        background = newColor;
                        enableApplyButton();
                    }
                } else if (s == 2) {
                    if (newColor != unknown) {
                        unknown = newColor;
                        colorscheme.remove(unknown);
                        colorscheme.put(Complexity.UNKNOWN, unknown);
                        enableApplyButton();
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
                        enableApplyButton();
                    }
                } else if (s == 4) {
                    if (newColor != npc) {
                        npc = newColor;
                        colorscheme.remove(npc);
                        colorscheme.put(Complexity.NPC, npc);
                        enableApplyButton();
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
                        enableApplyButton();
                    }
                } else if (s == 6) {
                    if (newColor != p) {
                        p = newColor;
                        colorscheme.remove(p);
                        colorscheme.put(Complexity.P, p);
                        enableApplyButton();
                    }
                } else if (s == 7) {
                    if (newColor != open) {
                        open = newColor;
                        colorscheme.remove(open);
                        colorscheme.put(Complexity.OPEN, open);
                        enableApplyButton();
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
                        enableApplyButton();
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
                        enableApplyButton();
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
                        enableApplyButton();
                    }
                    if (group) {
                        unknown = newColor;
                        open = newColor;
                    }
                }

            }
        });
        
        colorOptionsList
                .addListSelectionListener(new ListSelectionListener() {

                    /*
                     * Changes the color chooser to the color of the
                     * corresponding selection choosen in the list.
                     */
                    
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int i = colorOptionsList.getSelectedIndex();
                        if (i == 0) {
                            colorChooser.setColor(text);
                        } else if (i == 1) {
                            colorChooser.setColor(background);
                        } else if (i == 2) {
                            colorChooser.setColor(unknown);
                        } else if (i == 3) {
                            colorChooser.setColor(gic);
                        } else if (i == 4) {
                            colorChooser.setColor(npc);
                        } else if (i == 5) {
                            colorChooser.setColor(linear);
                        } else if (i == 6) {
                            colorChooser.setColor(p);
                        } else if (i == 7) {
                            colorChooser.setColor(open);
                        } else if (i == 8) {
                            colorChooser.setColor(conpc);
                        } else if (i == 9) {
                            colorChooser.setColor(nph);
                        } else if (i == 10) {
                            colorChooser.setColor(empty);
                        }

                    }
                });

        /*
         * Adds a button to the graph colors tab to get back the default 
         * colorsetting.
         */
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, gap, bigGap, hugeGap);
        c.gridwidth = 1;
        JButton gcSetDefaultButton = new JButton("Default colors");
        tabGraphcolors.add(gcSetDefaultButton, c);
        
        gcSetDefaultButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                 * Set default color scheme.
                 */
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
                groupcolors.setSelected(true);
                int i = colorOptionsList.getSelectedIndex();
                colorOptionsList.clearSelection();
                colorOptionsList.setSelectedIndex(i);

            }
        });

        /*
         * Adds a button to the graph colors tab to get the color blind 
         * color setting.
         */
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, gap, bigGap, gap);
        c.gridwidth = GridBagConstraints.RELATIVE;
        JButton gcColorBlind = new JButton("Set scheme for color blind");
        tabGraphcolors.add(gcColorBlind, c);
        
        gcColorBlind.addActionListener(new ActionListener() {
            
            //Set color blind color scheme.
            
            @Override
            public void actionPerformed(ActionEvent e) {
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
                int i = colorOptionsList.getSelectedIndex();
                colorOptionsList.clearSelection();
                colorOptionsList.setSelectedIndex(i);
            }
        });
        
        return tabGraphcolors;
    }
    
    /**
     * Creates the basic layout with tabbedpane and apply etc buttons.
     */
    private void createLayout() {
        setLayout(new BorderLayout());
        add(tabContainer, BorderLayout.CENTER);

        // bottomleft
        // to make the button as small as the right buttons
        JPanel bottomLeftPanel = new JPanel(new FlowLayout());

        JButton uiSetDefaultButton = new JButton("Default settings");
        bottomLeftPanel.add(uiSetDefaultButton, BorderLayout.LINE_START);
        
        uiSetDefaultButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                //Set all user interface options to default.
                zoom = UserSettings.getDefaultZoomLevel();
                zoomLevel.setValue(zoom);
                tabOrientation.setSelectedIndex(0);
                namingPreference.setSelectedIndex(0);
                // group
                themeSelector.setSelectedIndex(0);
                toolbar.setSelected(true);
            }
        });
        

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
                UserSettings.setNamingPref(namepref);
                UserSettings.setTabPlacement(tabPlacement);
                UserSettings.setTheme(javaTheme);
                UserSettings.setZoomLevel(zoom);
                UserSettings.setToolbarSetting(toolbar.isSelected());
                UserSettings.setColorScheme(colorscheme);
                colorscheme.clear();
            }
        });

        // merge bottom and add to general layout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomLeftPanel, BorderLayout.LINE_START);
        bottomPanel.add(bottomRightPanel, BorderLayout.LINE_END);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
    
    /**
     * Enables the apply button.
     */
    private void enableApplyButton() {
        applyButton.setEnabled(true);
    }
    
    /**
     * Close and dispose the dialogue.
     */
    public void closeDialog() {
        setVisible(false);
        dispose();
    }

}

/* EOF */

/*
 * Menu to select a preferred problem (for colouring).
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/ProblemsMenu.java,v 2.0 2011/09/25 12:37:13 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import teo.isgci.db.DataSet;
import teo.isgci.problem.Problem;
import teo.isgci.util.UserSettings;

public class ProblemsMenu extends JMenu implements ActionListener {
    protected Vector<JRadioButtonMenuItem> items;
    protected ISGCIMainFrame parent;
    protected ButtonGroup group;

    public ProblemsMenu(ISGCIMainFrame parent, String label) {
        super(label);
        this.parent = parent;
        items = new Vector<JRadioButtonMenuItem>();
        group = new ButtonGroup();

        addRadio("None", true);

        for (int i = 0; i < DataSet.problems.size(); i++) {
            addRadio(((Problem) DataSet.problems.elementAt(i))
                    .getName(), false);
        }
    }

    /**
     * Add a radiobutton to this menu.
     */
    private void addRadio(String s, boolean def) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(s, def);
        item.setActionCommand(s);
        item.addActionListener(this);
        add(item);
        group.add(item);
        items.addElement(item);
        
    }
    
    /**
     * Sets the active radio button to the given problem.
     * @param problem
     *              the name of the new problem
     */
    public void setProblem(String problem) {
        Enumeration<AbstractButton> buttons = group.getElements();
        for (int i = 0; i < group.getButtonCount(); i++) {
            AbstractButton b = buttons.nextElement();
            if (b.getText().equals(problem)) {
                group.setSelected(b.getModel(), true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        UserSettings.setProblem(DataSet.getProblem(event.getActionCommand()));
    }
}

/* EOF */


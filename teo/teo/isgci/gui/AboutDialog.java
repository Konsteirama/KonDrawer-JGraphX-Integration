/*
 * Display information about ISGCI.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/AboutDialog.java,v 2.1 2011/09/29 08:38:57 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import teo.isgci.db.DataSet;
import teo.isgci.util.Updatable;
import teo.isgci.util.UserSettings;

/**
 * Display information about ISGCI.
 */
public class AboutDialog extends JDialog implements Updatable {

    /**
     * Should change every time this class changes.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Display information about ISGCI.
     * @param parent
     *          The parent from which the dialog is opened.
     */
    public AboutDialog(ISGCIMainFrame parent) {
        super(parent, "About ISGCI", true);

        final int gap = 10;
        final int fontSize = 18;
        
        Insets insetsZero = new Insets(0, 0, 0, 0);
        Insets insetsTopMargin = new Insets(gap, 0, 0, 0);
        Font big = new Font("serif", Font.BOLD, fontSize);
        Container content = getContentPane();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gridbag);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(gap, gap, 0, gap);

        JLabel label1 = new JLabel(
                "Information System on Graph Classes and their Inclusions",
                JLabel.CENTER);
        label1.setFont(big);
        gridbag.setConstraints(label1, c);
        content.add(label1);

        c.insets = insetsTopMargin;
        JLabel label2 = new JLabel("Version 3.1", JLabel.CENTER);
        label2.setFont(big);
        gridbag.setConstraints(label2, c);
        content.add(label2);

        JLabel label2a = new JLabel("by H.N. de Ridder et al.", JLabel.CENTER);
        gridbag.setConstraints(label2a, c);
        content.add(label2a);

        c.insets = insetsZero;
        JLabel label2b = new JLabel("uses the JGraphT library", JLabel.CENTER);
        gridbag.setConstraints(label2b, c);
        content.add(label2b);

        c.insets = insetsTopMargin;
        JLabel label7 = new JLabel(DataSet.getNodeCount() + " classes, "
                + DataSet.getEdgeCount() + " inclusions", JLabel.CENTER);
        gridbag.setConstraints(label7, c);
        content.add(label7);

        c.insets = insetsZero;
        JLabel label5 = new JLabel(
                "Database generated : " + DataSet.getDate(), JLabel.CENTER);
        gridbag.setConstraints(label5, c);
        content.add(label5);

        c.insets = insetsTopMargin;
        JLabel label3 = new JLabel("http://www.graphclasses.org",
                JLabel.CENTER);
        gridbag.setConstraints(label3, c);
        content.add(label3);

        c.insets = new Insets(gap, 0, gap / 2, 0);
        JButton okButton = new JButton(" OK ");
        okButton.setToolTipText("Close this dialogue");
        gridbag.setConstraints(okButton, c);
        content.add(okButton);

        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });
        
        UserSettings.subscribeToOptionChanges(this);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    /**
     * Closes the dialog.
     */
    protected void closeDialog() {
        UserSettings.unsubscribe(this);
        setVisible(false);
        dispose();
    }

    @Override
    public void updateOptions() {
        try {
            UIManager.setLookAndFeel(UserSettings.getCurrentTheme());
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        SwingUtilities.updateComponentTreeUI(this);
        pack();
    }
}

/* EOF */

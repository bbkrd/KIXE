/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.kixe.options;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.junit.Assert;
import org.junit.Test;
import org.openide.util.NbPreferences;

/**
 *
 * @author s4504tw
 */
public class PerOptionsPanelTest {

    PerOptionsPanel instance = new PerOptionsPanel();

    @Test
    public void testLoadNo() {
        NbPreferences.forModule(PerOptionsPanelController.class).putBoolean(PerOptionsPanelController.PER_DEFAULT_METHOD, false);
        instance.load();
        JComboBox x = (JComboBox) ((JPanel) ((JPanel) instance.getComponents()[0]).getComponents()[0]).getComponents()[1];
        Assert.assertEquals("No", x.getSelectedItem().toString());
    }

    @Test
    public void testLoadYes() {
        NbPreferences.forModule(PerOptionsPanelController.class).putBoolean(PerOptionsPanelController.PER_DEFAULT_METHOD, true);
        instance.load();
        JComboBox x = (JComboBox) ((JPanel) ((JPanel) instance.getComponents()[0]).getComponents()[0]).getComponents()[1];
        Assert.assertEquals("Yes", x.getSelectedItem().toString());
    }

    @Test
    public void testStore() {
        JComboBox x = (JComboBox) ((JPanel) ((JPanel) instance.getComponents()[0]).getComponents()[0]).getComponents()[1];
        x.setSelectedItem("Yes");
        instance.store();
        Assert.assertTrue(NbPreferences.forModule(PerOptionsPanelController.class).getBoolean(PerOptionsPanelController.PER_DEFAULT_METHOD, false));
    }

    @Test
    public void testValid() {
        Assert.assertTrue(new PerOptionsPanel().valid());
    }

}

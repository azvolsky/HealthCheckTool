package org.healthCheckToolpkg;

/**
 * Created with IntelliJ IDEA.
 * User: azvolskiy
 * Date: 7/10/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class mainForm {
    private JTabbedPane jTabbedPane;

    public mainForm() {
        jTabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.WRAP_TAB_LAYOUT);

        jTabbedPane.add("Connections",new connectionTab(this));
//		jTabbedPane.setComponentAt(0, new JPanel());
        jTabbedPane.add("Configurations",new JPanel());
        jTabbedPane.add("Reports",new JPanel());
        jTabbedPane.add("Log",new JPanel());

    }

    public JTabbedPane getjTabbedPane() {
        return jTabbedPane;
    }


}

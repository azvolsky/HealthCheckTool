package org.healthCheckToolpkg;

/**
 * Created with IntelliJ IDEA.
 * User: azvolskiy
 * Date: 7/10/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class connectionTab extends JPanel  implements ActionListener {
    private		mainForm		mf;
    private 	credential 		cred				=	null;
    private		JLabel			protoLable			=	new JLabel("Proto:");
    private 	JTextField 		protocol 			= 	new JTextField(30);
    private		JLabel			hostLable			=	new JLabel("Host:");
    private 	JTextField 		host 				= 	new JTextField(30);
    private		JLabel			portLable			=	new JLabel("Port:");
    private 	JTextField 		port 				= 	new JTextField(30);
    private		JLabel			userLable			=	new JLabel("User:");
    private 	JTextField 		user 				= 	new JTextField(30);
    private		JLabel			passLable			=	new JLabel("Pass:");
    private 	JPasswordField 	pass 				= 	new JPasswordField(30);
    private 	JButton 		buttonAccept 		= 	new JButton("Connect");
    private 	JButton 		buttonClear 		= 	new JButton("Clear");
    private		JLabel			customerLable		=	new JLabel("Customer:");
    private 	JTextField 		customer 			= 	new JTextField(30);
    private 	JLabel			fileDropListLable 	= 	new JLabel("File:");
    private 	JComboBox		fileDropList;


    public connectionTab(mainForm mf) {
        this.mf = mf;
        setLayout(new GridLayout(8,2,6,6));
        add(protoLable);
        add(protocol);
        add(hostLable);
        add(host);
        add(portLable);
        add(port);
        add(userLable);
        add(user);
        add(passLable);
        add(pass);
        add(customerLable);
        add(customer);
        fileDropList = new JComboBox(makeFileList());
        add(fileDropListLable);
        add(fileDropList);
        add(buttonAccept);
        add(buttonClear);
        buttonAccept.addActionListener(this);
        buttonClear.addActionListener(this);
    }
    private String[] makeFileList() {
        File file = new File("D:/github/test/scripts");
        String filesList[] = new String[file.listFiles().length];
        for(int i=0;i<file.listFiles().length;i++){
            String str[] = file.listFiles()[i].toString().replace('\\', '/').split("/");
            filesList[i] = str[str.length-1];
        }
        return filesList;
    }


    // Show text when user presses ENTER.
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource().equals(buttonAccept)) {
            if(protocol.getText().isEmpty() || host.getText().isEmpty() || port.getText().isEmpty() ||
                    user.getText().isEmpty() || pass.getPassword()==null) {
                JOptionPane.showMessageDialog(new Frame(), "All fields should be filled");
            }
            else {
                cred = new credential(protocol.getText(),host.getText(),
                        new Integer(port.getText()),user.getText(),new String(pass.getPassword()));
                jmxHttpInvokerImpl jhi = new jmxHttpInvokerImpl(cred);
                jhi.setParamCustomerId(customer.getText());
                File file = new File("D:/github/test/scripts/"+fileDropList.getSelectedItem());
                StringBuffer str = new StringBuffer();
                try {
                    BufferedReader inputStream = new BufferedReader(new FileReader(file));

                    while(!inputStream.readLine().isEmpty()) {
                        str.append(inputStream.readLine());
                    }
                }
                catch(Exception e){

                }
                jhi.setParamoperationString(str.toString());
                try {
                    jhi.invoke();
                }
                catch(Exception e) {

                }
                mf.getjTabbedPane().setComponentAt(0, new resultTab(jhi.getResult()));

            }

        }
        if(ae.getSource().equals(buttonClear)) {
            host.setText("");
            port.setText("");
            user.setText("");
            pass.setText("");
        }


    }
    public credential getCred() {
        return cred;
    }
}
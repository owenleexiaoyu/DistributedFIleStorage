package com.owen.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.owen.storage.FileStorage;
import com.owen.utils.MyUtils;

public class FileStorageGUI extends JFrame implements ActionListener{
	private JButton node1;//�������
	private JButton node2;
	private JButton node3;
	private JButton node4;
	private JButton node5;
	private JButton node6;
	private JButton _node1;//�رսڵ�
	private JButton _node2;
	private JButton _node3;
	private JButton _node4;
	private JButton _node5;
	private JButton _node6;
	//������6���ڵ�
	private FileStorage storage1;
	private FileStorage storage2;
	private FileStorage storage3;
	private FileStorage storage4;
	private FileStorage storage5;
	private FileStorage storage6;
	public FileStorageGUI(){
		super("FileStorage");
		node1 = new JButton("�����ڵ�Node1");
		node2 = new JButton("�����ڵ�Node2");
		node3 = new JButton("�����ڵ�Node3");
		node4 = new JButton("�����ڵ�Node4");
		node5 = new JButton("�����ڵ�Node5");
		node6 = new JButton("�����ڵ�Node6");
		_node1 = new JButton("�رսڵ�Node1");
		_node2 = new JButton("�رսڵ�Node2");
		_node3 = new JButton("�رսڵ�Node3");
		_node4 = new JButton("�رսڵ�Node4");
		_node5 = new JButton("�رսڵ�Node5");
		_node6 = new JButton("�رսڵ�Node6");
		node1.addActionListener(this);
		node2.addActionListener(this);
		node3.addActionListener(this);
		node4.addActionListener(this);
		node5.addActionListener(this);
		node6.addActionListener(this);
		_node1.addActionListener(this);
		_node2.addActionListener(this);
		_node3.addActionListener(this);
		_node4.addActionListener(this);
		_node5.addActionListener(this);
		_node6.addActionListener(this);
		setLayout(new FlowLayout(FlowLayout.LEADING));
		getContentPane().add(node1);
		getContentPane().add(node2);
		getContentPane().add(node3);
		getContentPane().add(node4);
		getContentPane().add(node5);
		getContentPane().add(node6);
		getContentPane().add(_node1);
		getContentPane().add(_node2);
		getContentPane().add(_node3);
		getContentPane().add(_node4);
		getContentPane().add(_node5);
		getContentPane().add(_node6);
		setSize(400, 300);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - 720) / 2, (d.height - 400) / 2);
		setVisible(true); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        //����ʱ����1,2,3,4�ĸ���㣬֮��̬���5,6�������
        storage1 = new FileStorage(MyUtils.getProperty("nodes\\node1.properties"));	
		node1.setEnabled(false);
		storage2 = new FileStorage(MyUtils.getProperty("nodes\\node2.properties"));
		node2.setEnabled(false);
		storage3 = new FileStorage(MyUtils.getProperty("nodes\\node3.properties"));
		node3.setEnabled(false);
		storage4 = new FileStorage(MyUtils.getProperty("nodes\\node4.properties"));
		node4.setEnabled(false);
		_node5.setEnabled(false);
		_node6.setEnabled(false);
		
		//�ڹرմ���ʱֹͣ���еĽ��
		this.addWindowListener(new MyWindowsListener());
	}
	public static void main(String[] args) {
		new FileStorageGUI();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(node1.equals(arg0.getSource())){
			//��ʼ���洢���
			storage1 = new FileStorage(MyUtils.getProperty("nodes\\node1.properties"));	
			node1.setEnabled(false);
			_node1.setEnabled(true);
		}
		if(node2.equals(arg0.getSource())){
			storage2 = new FileStorage(MyUtils.getProperty("nodes\\node2.properties"));
			node2.setEnabled(false);
			_node2.setEnabled(true);
		}
		if(node3.equals(arg0.getSource())){
			storage3 = new FileStorage(MyUtils.getProperty("nodes\\node3.properties"));
			node3.setEnabled(false);
			_node3.setEnabled(true);
		}
		if(node4.equals(arg0.getSource())){
			storage4 = new FileStorage(MyUtils.getProperty("nodes\\node4.properties"));
			node4.setEnabled(false);
			_node4.setEnabled(true);
		}
		if(node5.equals(arg0.getSource())){
			storage5 = new FileStorage(MyUtils.getProperty("nodes\\node5.properties"));
			node5.setEnabled(false);
			_node5.setEnabled(true);
		}
		if(node6.equals(arg0.getSource())){
			storage6 = new FileStorage(MyUtils.getProperty("nodes\\node6.properties"));
			node6.setEnabled(false);
			_node6.setEnabled(true);
		}
		if(_node1.equals(arg0.getSource())){
			//�رսڵ�1
			storage1.closeNode();
			storage1 = null;
			node1.setEnabled(true);
			_node1.setEnabled(false);
		}
		if(_node2.equals(arg0.getSource())){
			//�رսڵ�2
			storage2.closeNode();
			storage2 = null;
			node2.setEnabled(true);
			_node2.setEnabled(false);
		}
		if(_node3.equals(arg0.getSource())){
			//�رսڵ�3
			storage3.closeNode();
			storage3 = null;
			node3.setEnabled(true);
			_node3.setEnabled(false);
		}
		if(_node4.equals(arg0.getSource())){
			//�رսڵ�4
			storage4.closeNode();
			storage4 = null;
			node4.setEnabled(true);
			_node4.setEnabled(false);
		}
		if(_node5.equals(arg0.getSource())){
			//�رսڵ�5
			storage5.closeNode();
			storage5 = null;
			node5.setEnabled(true);
			_node5.setEnabled(false);
		}
		if(_node6.equals(arg0.getSource())){
			//�رսڵ�6
			storage6.closeNode();
			storage6 = null;
			node6.setEnabled(true);
			_node6.setEnabled(false);
		}
	}
	class MyWindowsListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
//			System.out.println("���ڹر���");
			if(storage1 != null){
				storage1.closeNode();
				storage1 = null;
			}
			if(storage2 != null){
				storage2.closeNode();
				storage2 = null;
			}
			if(storage3 != null){
				storage3.closeNode();
				storage3 = null;
			}
			if(storage4 != null){
				storage4.closeNode();
				storage4 = null;
			}
			if(storage5 != null){
				storage5.closeNode();
				storage5 = null;
			}
			if(storage6 != null){
				storage6.closeNode();
				storage6 = null;
			}
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
		
	}
}

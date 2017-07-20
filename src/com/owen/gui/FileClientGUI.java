package com.owen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.owen.client.FileClient;

public class FileClientGUI extends JFrame{
	private FileClient fileClient;
	JScrollPane jScrollPane = null;
	private JButton btnUpdate;//�ϴ��ļ��İ�ť
	private JButton btnDownload;//�����ļ��İ�ť
	private JButton btnDelete;//ɾ���ļ��İ�ť
	private JLabel lbUUID;
	private JTextField tfUUID;//���غ�ɾ����ʱ������uuid
	private JTextArea taMsg;//��ʾһЩ��Ϣ
	public FileClientGUI(String title){
		super(title);
		//��ʼ��һ��FileClient���󣬵����ͬ�İ�ť������FileClient�Ĳ�ͬ����
		fileClient = new FileClient();
		//��ʼ���ؼ�
		btnUpdate = new JButton("�ϴ��ļ�"); 
		btnDownload = new JButton("�����ļ�");
		btnDelete = new JButton("ɾ���ļ�");
		lbUUID = new JLabel("UUID");
		tfUUID = new JTextField();
		tfUUID.setPreferredSize(new Dimension(220, 28));
		taMsg = new JTextArea();
		taMsg.setSize(480, 350);
		jScrollPane = new JScrollPane(taMsg);
		// ���ù�������
		jScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); 
		//����JPanel�Ĳ���ΪFlowLayout
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEADING));
		//��Ӱ�ť�ؼ������JPanel��
        jp.add(btnUpdate); 
        jp.add(lbUUID);
        jp.add(tfUUID);
        jp.add(btnDownload); 
        jp.add(btnDelete); 
        //��JPanel��ӵ�����������
        getContentPane().add(jp,BorderLayout.NORTH);
        getContentPane().add(jScrollPane,BorderLayout.CENTER);
        setSize(550, 400);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - 720) / 2, (d.height - 400) / 2);
		setVisible(true); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        btnUpdate.addActionListener(new UpdateActionListener());
        btnDownload.addActionListener(new DownloadActionListener());
        btnDelete.addActionListener(new DeleteActionListener());
	}
	public static void main(String[] args) {
		new FileClientGUI("FileClient");
	}
	/**
	 * �ϴ��ļ���ť���¼�����
	 * ���ļ�ѡ������ѡ��һ���ļ�����ȡ�����ļ��������ϴ�
	 * @author Administrator
	 *
	 */
	class UpdateActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc=new JFileChooser();  
	        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
	        jfc.showDialog(new JLabel(), "ѡ��");  
	        File file=jfc.getSelectedFile();  
	        if(file == null){
	        	taMsg.append("δѡ���ļ�\n");
	        }
	        else if(file.isDirectory()){
	        	taMsg.append("�ݲ�֧���ϴ��ļ��У���ѡ��һ���ļ�\n");
	        }
	        else{
	        	taMsg.append("�ϴ��ļ�:"+file.getName()+"\n");
		        String uuid = fileClient.update(file.getAbsolutePath());
		        if(uuid != null){
		        	taMsg.append("�ļ��ϴ��ɹ�,uuidΪ��"+uuid+"\n");
		        }else{
		        	taMsg.append("�ļ��ϴ�ʧ��\n");
		        }
	        }
		}
	}
	/**
	 * �����ļ���ť�ĵ���¼����������ļ���uuid�������ļ�����
	 * @author Administrator
	 *
	 */
	class DownloadActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String uuid = tfUUID.getText();
			if(uuid == null || uuid.equals("")){
				taMsg.append("�������ļ�uuid\n");
			}else{
				taMsg.append("�����ļ�:"+uuid+"\n");
				boolean success = fileClient.download(uuid);
				taMsg.append("�ļ�����"+(success?"�ɹ�\n":"ʧ��\n"));
			}
		}
	}
	/**
	 * ɾ���ļ���ť�ĵ���¼����������ļ���uuid�������ļ�ɾ��
	 * @author Administrator
	 *
	 */
	class DeleteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String uuid = tfUUID.getText();
			if(uuid == null || uuid.equals("")){
				taMsg.append("�������ļ�uuid\n");
			}else{
				taMsg.append("ɾ���ļ�:"+uuid+"\n");
				boolean success = fileClient.delete(uuid);
				taMsg.append("�ļ�ɾ��"+(success?"�ɹ�\n":"ʧ��\n"));
			}
		}
	}

}

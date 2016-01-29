package com.caigin.hlb.pay.tool;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
public class sftp {
    protected String host="222.73.39.37";//sftp������ip
    protected String username="200004227922";//�û���
    protected String password;//����
    protected String privateKey="C:\\data\\id_rsa";//��Կ�ļ�·��
    protected String passphrase;//��Կ����
    protected int port = 50022;//Ĭ�ϵ�sftp�˿ں���22
    /**
     * ��ȡ����
     * @return channel
     */
    public ChannelSftp connectSFTP() {
        JSch jsch = new JSch();
        Channel channel = null;
        try {
            if (privateKey != null && !"".equals(privateKey)) {
                //ʹ����Կ��֤��ʽ����Կ����ʹ�п������Կ��Ҳ������û�п������Կ
                if (passphrase != null && "".equals(passphrase)) {
                    jsch.addIdentity(privateKey, passphrase);
                } else {
                    jsch.addIdentity(privateKey);
                }
            }
            Session session = jsch.getSession(username, host, port);
            if (password != null && !"".equals(password)) {
                session.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            session.setConfig(sshConfig);
            // session.setTimeout(timeout);
            session.setServerAliveInterval(92000);
            session.connect();
            //����sftpָ��Ҫ�򿪵�������sftp����
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp���ӳɹ�");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return (ChannelSftp) channel;
    }
    
    /**
     * �ϴ��ļ�
     * 
     * @param directory
     *            �ϴ���Ŀ¼
     * @param uploadFile
     *            Ҫ�ϴ����ļ�
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
            System.out.println("�ϴ����!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * @throws SftpException 
 * 
 */
    public void ls(String directory,ChannelSftp sftp) throws SftpException
    {
    	sftp.cd(directory);
    	Vector v=sftp.ls("*.*");
        for(int i=0;i<v.size();i++)
        {
         System.out.println(v.get(i));
        }
    }
    /**
     * �����ļ�
     * 
     * @param directory
     *            ����Ŀ¼
     * @param downloadFile
     *            ���ص��ļ�
     * @param saveFile
     *            ���ڱ��ص�·��
     * @param sftp
     */
    public void download(String directory, String downloadFile,
            String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.get(downloadFile,saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ���ļ�
     * 
     * @param directory
     *            Ҫɾ���ļ�����Ŀ¼
     * @param deleteFile
     *            Ҫɾ�����ļ�
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void disconnected(ChannelSftp sftp){
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sftp.disconnect();
        }
    }
    //����sftp����
    public static void main(String args[]) throws SftpException 
    { 
    	System.out.println("����sftp");
    	sftp s=new sftp();
    	//s.upload("/upload/", "C:\\data\\4100.zip",s.connectSFTP());
    	 s.ls("/upload/",s.connectSFTP());
    } 
    
}
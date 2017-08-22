package com.io.file;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午2:39
 */
public class Note extends JFrame implements ActionListener {

    //定义所需组件
    JTextArea jta = null;
    //菜单条
    JMenuBar jmb = null;
    JMenu jm1 = null,jm2 = null;
    JMenuItem jmi1 = null,jmi2=null;
    JScrollPane jsp = null;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Note n = new Note();
    }

    public Note(){
        jta = new JTextArea();
        jmb = new JMenuBar();
        jm1 = new JMenu("打开");
        jm2 = new JMenu("保存");

        jm1.setMnemonic('F');
        jmi1 = new JMenuItem("打开",new ImageIcon());//可以放图片
        jmi2 = new JMenuItem("保存");

        //注册监听
        jmi1.addActionListener(this);
        jmi1.setActionCommand("打开");
        jmi2.addActionListener(this);
        jmi2.setActionCommand("保存");
        //加入
        this.setJMenuBar(jmb);
        //把jm1放入jmb
        jmb.add(jm1);
        jmb.add(jm2);
        //把item放入到Menu
        jm1.add(jmi1);
        jm2.add(jmi2);
//		jm1.add(jmi2);

        jsp = new JScrollPane (jta);

        this.add(jsp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent N) {
        // TODO Auto-generated method stub
        if (N.getActionCommand().equals("打开")){
//			System.out.println("打开");

            //推荐JFileChooser 组件
            JFileChooser jc = new JFileChooser();
            //设置名字
            jc.setDialogTitle("请选择文件...");
            //默认方式
            jc.showOpenDialog(null);
            jc.setVisible(true);

            //得知道用户选择的文件 绝对路径
            String filePath = jc.getSelectedFile().getAbsolutePath();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(filePath));

                //从文件读取信息显示到jta
                String s ="";
                String result = "";
                while((s=br.readLine())!=null){

                    result+=s+"\r\n";

                }
                //输出到jta
                jta.setText(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }

        }
        else if (N.getActionCommand().equals("保存")){
            System.out.println("保存");
            //出现保存对话框
            JFileChooser jc = new JFileChooser();
            jc.setDialogTitle("另存为...");
            //按默认方式显示
            jc.showSaveDialog(null);
            jc.setVisible(true);

            //得到用户希望把文件保存到何处
            String file = jc.getSelectedFile().getAbsolutePath();
            BufferedWriter bw = null;
            //准备写入到指定目录下
            try{
                bw = new BufferedWriter(new FileWriter(file));

                bw.write(this.jta.getText());

            }catch (Exception e){
                e.printStackTrace();
            }finally{
                try {
                    bw.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }

        }
    }
}

package myswing;

import myutils.DFormat;
import myutils.FileAnalysis;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Enumeration;
import java.util.List;

public class Frame extends JFrame {

    private File openFile;//文件类
    private File outFile;//文档输出位置
    private FileInputStream fileInputStream;       //字节文件输入流
    private FileOutputStream fileOutputStream;     //字节文件输出流
    private OutputStreamWriter outputStreamWriter; //字符文件输出流

    public Frame(String title){
        super(title);
        setVisible(true);
        setSize(600 , 400);//设置窗体大小
        setResizable(false);//设为不可改变大小
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel p1=new JPanel() ; //创建容器
        this.add(p1);
        p1.setLayout(null);//设置对齐方式
        initFrame(p1);
        validate();
        repaint();
    }

    /**
     * 对容器进行界面初始化
     * @param p1 容器
     */
    private void initFrame(JPanel p1) {

        JLabel jl_name = new JLabel("文件名：");
        jl_name.setBounds(10,10,120,40);

        final JLabel jl_filename = new JLabel("当前未选择文件");
        jl_filename.setBounds(140,10,420,40);

        JLabel jl_outname = new JLabel("输出文件名：");
        jl_outname.setBounds(10,60,120,40);

        final JTextField jt_outpath = new JTextField();
        jt_outpath.setBounds(140,60,420,40);

        JLabel jl_outtype = new JLabel("输出文件类型：");
        jl_outtype.setBounds(10,120,120,40);

        final JComboBox comboBox=new JComboBox();
        comboBox.setBounds(140,120,420,40);
        comboBox.addItem("doc");
        comboBox.addItem("docx");

        JLabel jl_Dtype = new JLabel("输出文献格式：");
        jl_Dtype.setBounds(10,180,120,40);

        final JComboBox DBox=new JComboBox();
        DBox.setBounds(140,180,420,40);
        DBox.addItem("国内标准");
        DBox.addItem("IEEE");
        DBox.addItem("ACM");
        DBox.addItem("SCI");

        JButton btnBrowse = new JButton("文件选择");    //选择文件
        btnBrowse.setBounds(10,240,570,40);
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(); //文件选择
                chooser.showOpenDialog(chooser);        //打开文件选择窗
                openFile = chooser.getSelectedFile();  	//获取选择的文件
                if (openFile != null){
                    //对选择的文件格式进行校验
                    String filePath = openFile.getPath();
                    String[] split = filePath.split("\\.");
                    if (!split[split.length-1].equals("docx")) {
                        JOptionPane.showMessageDialog(null, "请选择docx格式的文件", "文件格式错误", JOptionPane.ERROR_MESSAGE);
                    }else{
                        jl_filename.setText(filePath);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "未选择文件", "警告", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnDivide = new JButton("执行文献划分");    //选择文件
        btnDivide.setBounds(10,300,570,40);
        btnDivide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String outpath = jt_outpath.getText();
                //如果未填入路径
                if (outpath==null){
                    JOptionPane.showMessageDialog(null, "请输入文件保存位置", "警告", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String outname = jt_outpath.getText() + "." + comboBox.getSelectedItem();
                System.out.println(outname);
                String stander = (String) DBox.getSelectedItem();
                System.out.println(stander);
                List<String> analysis = FileAnalysis.Analysis(openFile);
                for (int i=0;i<analysis.size();i++){
                    analysis.set(i,"[" + (i+1) + "]" + " " + analysis.get(i));
                }
                for (int i=0;i<analysis.size();i++){
                    System.out.println(analysis.get(i));
                    analysis.set(i,DFormat.getDdata(analysis.get(i),DFormat.getStandardType(analysis.get(i)),stander));
                }

                String path = "D:\\KDR\\" + outname;
                try {
                    //创建文件
                    FileAnalysis.createWord("D:\\KDR",outname);
                    FileAnalysis.writeDataDocx(path,analysis,false,12);
                    JOptionPane.showMessageDialog(null,"文件解析成功！请前往D:\\KDR查看");
                }catch (Exception e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "创建文件失败'D:\\KDR'" + outname, "错误", JOptionPane.ERROR_MESSAGE);
                }



            }
        });

        p1.add(jl_Dtype);
        p1.add(DBox);
        p1.add(comboBox);
        p1.add(jl_outtype);
        p1.add(btnDivide);
        p1.add(jt_outpath);
        p1.add(jl_outname);
        p1.add(jl_filename);
        p1.add(jl_name);
        p1.add(btnBrowse);

    }

    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    public static void main(String[] args) {
        //界面美化,使用了https://github.com/JackJiang2011/beautyeye美化包
        InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 16));
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            //BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("set skin fail!");
        }
        new Frame("文献划分");
    }

}

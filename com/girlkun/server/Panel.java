package com.girlkun.server;

import com.girlkun.data.DataGame;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.utils.CreateMobDataFile;
import static com.girlkun.utils.CreateMobDataFile.createMobDataFile;
import com.girlkun.utils.ImageToByteArrayAndSaveExample;
import com.girlkun.utils.ImageUtil2;
import com.girlkun.utils.Logger;
import com.girlkun.utils.ImageUtil;
//import com.sun.javafx.util.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
/*
 *
 * Author BTH siêu cấp zíp pro
 *
 */
import javax.swing.*;

public class Panel extends JPanel implements ActionListener {

    private JButton baotri, thaydoiexp, buffitem, thaydoisk, load, readmob, readeff;
    private JPanel addPanel;

    public Panel() {
        JPanel btri = new JPanel();
        btri.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        btri.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(btri);

        baotri = new JButton("Bảo trì");
        baotri.addActionListener(this);
        btri.add(baotri);

        JPanel exp = new JPanel();
        exp.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        exp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(exp);

        thaydoiexp = new JButton("Thay đổi exp");
        thaydoiexp.addActionListener(this);
        exp.add(thaydoiexp);

        JPanel buff = new JPanel();
        buff.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        buff.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(buff);

        buffitem = new JButton("Buff item");
        buffitem.addActionListener(this);
        buff.add(buffitem);

        JPanel sk = new JPanel();
        sk.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        sk.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(sk);

        thaydoisk = new JButton("PNG To Byte[]");
        thaydoisk.addActionListener(this);
        sk.add(thaydoisk);

        JPanel loaddb = new JPanel();
        loaddb.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        loaddb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(loaddb);

        load = new JButton("Load Data");
        load.addActionListener(this);
        loaddb.add(load);

        JPanel rm = new JPanel();
        rm.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        rm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(rm);

        readmob = new JButton("Read mob");
        readmob.addActionListener(this);
        rm.add(readmob);

        JPanel le = new JPanel();
        le.setBackground(new Color(240, 240, 240)); // Màu nền nhạt
        le.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Khoảng cách viền
        add(le);

        readeff = new JButton("Read eff");
        readeff.addActionListener(this);
        le.add(readeff);

    }

    public void actionPerformed(ActionEvent e) {
        JTextField namedpl = new JTextField(10);
        JTextField idit = new JTextField(10);
        JTextField idop = new JTextField(10);
        JTextField par = new JTextField(10);
        JTextField quanity = new JTextField(10);

        namedpl = new JTextField(10);
        namedpl.setBounds(300, 20, 300, 60); // Tăng kích thước
        add(namedpl);

        idit = new JTextField(10);
        idit.setBounds(300, 100, 300, 60);
        add(idit);

        idop = new JTextField(10);
        idop.setBounds(300, 180, 300, 60);
        add(idop);

        par = new JTextField(10);
        par.setBounds(300, 260, 300, 60);
        add(par);

        quanity = new JTextField(10);
        quanity.setBounds(300, 340, 300, 60);
        add(quanity);

        // Panel chứa nhãn và ô nhập thông tin
        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(20, 20, 260, 380); // Tăng kích thước
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(inputPanel);

        inputPanel.add(new JLabel("Nhập id người chơi:"));
        inputPanel.add(namedpl);
        inputPanel.add(new JLabel("Nhập id item:"));
        inputPanel.add(idit);
        inputPanel.add(new JLabel("Nhập ID Option:"));
        inputPanel.add(idop);
        inputPanel.add(new JLabel("Nhập Param Option:"));
        inputPanel.add(par);
        inputPanel.add(new JLabel("Nhập số lượng item:"));
        inputPanel.add(quanity);

        if (e.getSource() == baotri) {
            String imagePath = "D:/13.png"; // Đường dẫn đến tệp ảnh chứa bộ phận của mob
            String txtPath = "D:/data.txt"; // Đường dẫn đến tệp văn bản chứa thông số của mob
            String outputDataPath = "D:/mob_data"; // Đường dẫn đến tệp dữ liệu mob đầu ra
            int zoomlv = 2; // Zoom Level
            int mob = 13; // ID của mob

            createMobDataFile(imagePath, txtPath, outputDataPath, zoomlv, mob);
            Logger.error("ok");
        } else if (e.getSource() == load) {
            Player pl = Client.gI().getPlayer("admin");
            if (pl != null) {
                Manager.loadPart();
                DataGame.updateData(pl.getSession());
                DataGame.updateSkill(pl.getSession());
                DataGame.updateMap(pl.getSession());
                Client.gI().kickSession(pl.getSession());
                String message = "<html><font color='orange'>Load Database thành công</font></html>";
                JOptionPane.showMessageDialog(null, message);
            } else {
                String message = "<html><font color='red'>Người chơi không tồn tại</font></html>";
                JOptionPane.showMessageDialog(null, message);
            }
        } else if (e.getSource() == readmob) {
            String mob = JOptionPane.showInputDialog(this, "Nhập id mob\n");
            if (mob != null) {
                ImageUtil2.readmob(4, Integer.parseInt(mob));
                String message = "<html><font color='red'>Read mod thành công</font></html>";
            }

        } else if (e.getSource() == readeff) {
            String eff = JOptionPane.showInputDialog(this, "Nhập id eff\n");
            if (eff != null) {
                ImageUtil.readEff(Integer.parseInt(eff), 2);
                String message = "<html><font color='red'>Read eff thành công</font></html>";
            }

        } else if (e.getSource() == thaydoiexp) {
            String exp = JOptionPane.showInputDialog(this, "Nhập Exp muốn thay đổi\n"
                    + "EXP hiện tại là :" + Manager.RATE_EXP_SERVER);
            if (exp != null) {
                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
                Logger.error("EXP hiện tại là :" + exp);
            }
        } else if (e.getSource() == buffitem) {
            int option = JOptionPane.showOptionDialog(null, inputPanel, "Buff item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            if (option == JOptionPane.OK_OPTION) {
                String name = namedpl.getText();
                String vpl = idit.getText();
                String idopl = idop.getText();
                String paraml = par.getText();
                String sll = quanity.getText();
                if (!name.isEmpty()) {
                    // Xử lý giá trị ID người chơi ở đây
                }
                if (!vpl.isEmpty()) {
                    // Xử lý giá trị ID item ở đây
                }
                if (!idopl.isEmpty()) {
                    // Xử lý giá trị ID Option ở đây
                }
                if (!paraml.isEmpty()) {
                    // Xử lý giá trị Param Option ở đây
                }
                if (!sll.isEmpty()) {
                    // Xử lý giá trị số lượng item ở đây
                }
                // Tách chuỗi idopl thành mảng các số
                String[] idOptionArray = idopl.split(" ");
                // Tách chuỗi paraml thành mảng các số
                String[] slOptionArray = paraml.split(" ");
                if (idOptionArray.length == slOptionArray.length) {
                    List<Integer> idOptionBuffList = new ArrayList<>();
                    List<Integer> slOptionBuffList = new ArrayList<>();
                    int slItemBuff = Integer.parseInt(sll);
                    Player pBuffItem = Client.gI().getPlayer(name);
                    if (pBuffItem != null) {
                        String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                        for (int i = 0; i < idOptionArray.length; i++) {
                            // Chuyển đổi từng phần tử của mảng thành kiểu số
                            int idOptionBuff = Integer.parseInt(idOptionArray[i]);
                            int slOptionBuff = Integer.parseInt(slOptionArray[i]);
                            idOptionBuffList.add(idOptionBuff);
                            slOptionBuffList.add(slOptionBuff);
                        }
                        int idItemBuff = Integer.parseInt(vpl);
                        if (idItemBuff == -1) {
                            pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, Inventory.LIMIT_GOLD);
                            txtBuff += slItemBuff + " vàng\b";
                            Service.getInstance().sendMoney(pBuffItem);
                        } else if (idItemBuff == -2) {
                            pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                            txtBuff += slItemBuff + " ngọc\b";
                            Service.getInstance().sendMoney(pBuffItem);
                        } else if (idItemBuff == -3) {
                            pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                            txtBuff += slItemBuff + " ngọc khóa\b";
                            Service.getInstance().sendMoney(pBuffItem);
                        } else {
                            Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                            for (int i = 0; i < idOptionBuffList.size(); i++) {
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuffList.get(i), slOptionBuffList.get(i)));
                            }
                            itemBuffTemplate.quantity = slItemBuff;
                            txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                            InventoryServiceNew.gI().sendItemBags(pBuffItem);
                        }
                        NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                        if (pBuffItem.id != pBuffItem.id) {
                            NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                        }
                        String message = "<html><font color='blue'>Bạn đã Buff item thành công</font></html>";
                        JOptionPane.showMessageDialog(null, message);
                        Logger.error("Tên người chơi: " + name);
                        Logger.error("\nID vật phẩm: " + vpl);
                        Logger.error("\nID Option: " + idopl);
                        Logger.error("\nParam: " + paraml);
                        Logger.error("\nSố lượng: " + sll);
                    } else {
                        String message = "<html><font color='red'>Người chơi không online hoặc bạn đã nhập sai tên người chơi</font></html>";
                        JOptionPane.showMessageDialog(null, message);
                    }
                } else {
                    String message = "<html><font color='red'>Lỗ"
                            + "i: Số lượng ID Option và Param không khớp.</font></html>";
                    JOptionPane.showMessageDialog(null, message);
                }
            }
        } else if (e.getSource() == thaydoisk) {
            String sk = JOptionPane.showInputDialog(this, "Nhập id eff\n");
            if (sk != null) {
                ImageToByteArrayAndSaveExample.main();
                Logger.error("Chuyển thành công eff :" + sk);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(650, 420); // Tăng kích thước của Panel chính
    }

}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */

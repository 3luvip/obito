package com.girlkun.server;

import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.ServerManager;
import com.girlkun.utils.Logger;
import java.awt.Button;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;
import jdk.internal.org.jline.utils.Log;

public class obito extends JFrame {

    private Preferences preferences;
    private JLabel plCountLabel;
    private JLabel threadCountLabel;
    private JTextField minutesField;
    private JLabel messageLabel;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int remainingSeconds;
    private ButtonGroup maintenanceGroup;
// Thêm checkbox
    private JCheckBox maintenanceOption1;
    private JCheckBox maintenanceOption2;
    private JLabel info;
    public static boolean isRunning;

    public obito() {
        preferences = Preferences.userNodeForPackage(obito.class);
        setTitle("Ngọc Rồng By Obito");
        setSize(310, 150);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        JButton maintenanceButton = new JButton("Bảo trì");
        maintenanceButton.addActionListener(e -> showMaintenanceDialog());
        panel.add(maintenanceButton);
        JButton maintenanceButton1 = new JButton("Kick Player");
        maintenanceButton1.addActionListener(e -> kick());
        panel.add(maintenanceButton1);
        JButton maintenanceButton2 = new JButton(" Exp ");
        maintenanceButton2.addActionListener(e -> tnsm());
        panel.add(maintenanceButton2);
        
             
        
        JLabel jLabel2 = new JLabel("");
        panel.add(jLabel2);
        info = new JLabel("");
        // Đọc giá trị từ tệp tin
        try (BufferedReader reader = new BufferedReader(new FileReader("maintenanceConfig.txt"))) {
            String hoursLine = reader.readLine();
            String minutesLine = reader.readLine();

            int hours = Integer.parseInt(hoursLine);
            int minutes = Integer.parseInt(minutesLine);

            // Thêm giá trị vào DefaultComboBoxModel
            DefaultComboBoxModel<Integer> hoursModel = new DefaultComboBoxModel<>();
            for (int i = -1; i < 24; i++) {
                hoursModel.addElement(i);
            }
            JComboBox<Integer> hoursComboBox = new JComboBox<>(hoursModel);
            panel.add(hoursComboBox);
            hoursComboBox.setSelectedItem(hours);

            // Thêm giá trị vào DefaultComboBoxModel
            DefaultComboBoxModel<Integer> minutesModel = new DefaultComboBoxModel<>();
            for (int i = -1; i < 60; i++) {
                minutesModel.addElement(i);
            }
            JComboBox<Integer> minutesComboBox = new JComboBox<>(minutesModel);
            panel.add(minutesComboBox);
            minutesComboBox.setSelectedItem(minutes);
            JButton scheduleButton2 = new JButton("Hẹn giờ bảo trì");
            scheduleButton2.addActionListener(e -> scheduleMaintenance(hoursComboBox, minutesComboBox));
            panel.add(scheduleButton2);
            if (hours != -1 && minutes != -1) {
                scheduleMaintenance(hoursComboBox, minutesComboBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageLabel = new JLabel();
        panel.add(messageLabel);

        countdownLabel = new JLabel();
        panel.add(countdownLabel);

        panel.add(info);
        threadCountLabel = new JLabel("Thread : ");
        panel.add(threadCountLabel);
        plCountLabel = new JLabel("Online :");
        panel.add(plCountLabel);

        ScheduledExecutorService threadCountExecutor = Executors.newSingleThreadScheduledExecutor();
        threadCountExecutor.scheduleAtFixedRate(() -> {
            int threadCount = Thread.activeCount();
            threadCountLabel.setText("Số Thread: " + threadCount);
        }, 1, 1, TimeUnit.SECONDS);

        ScheduledExecutorService plCountExecutor = Executors.newSingleThreadScheduledExecutor();
        plCountExecutor.scheduleAtFixedRate(() -> {
            int plcount = Client.gI().getPlayers().size();
            plCountLabel.setText("Online : " + plcount);
        }, 5, 1, TimeUnit.SECONDS);
        setVisible(true);
        messageLabel.setText("");
        ServerManager.gI().run();
        // Đọc giá trị từ tệp

    }

    private void showMaintenanceDialog() {
        try {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Bắt đầu bảo trì?", "Bảo trì", dialogButton);
            if (dialogResult == 0) {
                Logger.error("Server tiến hành bảo trì");
                Maintenance.gI().start(5);

            } else {
                System.out.println("No Option");
            }
        } catch (Exception e) {

        }

    }

    private void kick() {
        new Thread(() -> {
            Client.gI().close();
        }).start();

    }

    private void tnsm() {
        String exp = JOptionPane.showInputDialog(this, "Bảng Exp Server\n"
                + "Exp Server hiện tại: " + Manager.RATE_EXP_SERVER);
        if (exp != null) {
            Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
            Logger.error("Exp hiện tại là: " + exp + "\n");
        }

    }
  
    private void scheduleMaintenance() {
        String minutesStr = minutesField.getText();
        try {
            int minutes = Integer.parseInt(minutesStr);
            if (minutes <= 0) {
                messageLabel.setText("Số phút phải lớn hơn 0");
                return;
            }
            // Lưu giá trị vào tệp
            try {
                File file = new File("maintenanceTime.txt");
                FileWriter fw = new FileWriter(file);
                fw.write(String.valueOf(minutes));
                fw.close();
            } catch (Exception e) {

            }

            long delay = minutes * 60L * 1000L;
            remainingSeconds = minutes * 60;
            countdownLabel.setText("Thời gian còn lại: " + formatTime(remainingSeconds));
            countdownTimer = new Timer(1000, e -> {
                remainingSeconds--;
                countdownLabel.setText("Thời gian còn lại: " + formatTime(remainingSeconds));
                if (remainingSeconds == 0) {
                    countdownTimer.stop();
                    Maintenance.gI().start(15);
                    messageLabel.setText("");
                    countdownLabel.setText("");
                }
            });
            countdownTimer.start();

            messageLabel.setText("Đã hẹn bảo trì sau " + minutes + " phút");
        } catch (NumberFormatException e) {
            String error = e.getMessage();
            if (error.equals("For input string: \"\"")) {
                JOptionPane.showMessageDialog(null, "Không được để trống");
            } else {
                JOptionPane.showMessageDialog(null, "Bạn nhập sai phút");
            }

        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void confirmExit() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát chương trình?", "Thoát", dialogButton);
        if (dialogResult == 0) {
            System.exit(0);
        }
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        if (operation == JFrame.EXIT_ON_CLOSE) {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    confirmExit();
                }
            });
        } else {
            super.setDefaultCloseOperation(operation);
        }
    }

    private void scheduleMaintenance(JComboBox<Integer> hoursComboBox, JComboBox<Integer> minutesComboBox) {
        int hours = hoursComboBox.getItemAt(hoursComboBox.getSelectedIndex());
        int minutes = minutesComboBox.getItemAt(minutesComboBox.getSelectedIndex());
        if (minutes == -1 || hours == -1) {
            JOptionPane.showMessageDialog(this, "Thời gian sai");
            return;
        }
        // Ghi giá trị vào tệp tin
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("maintenanceConfig.txt"))) {
            writer.write(hours + "\n");
            writer.write(minutes + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AtomicBoolean timeReached = new AtomicBoolean(false); // Sử dụng AtomicBoolean để đảm bảo tính nhất quán trong thread
        info.setText("Auto Bảo Trì " +": " + hours + ":" + minutes);
        new Thread(() -> {
            while (!timeReached.get()) { // Kiểm tra điều kiện dừng
                try {
                    LocalTime currentTime = LocalTime.now();
                    int hourss = hoursComboBox.getItemAt(hoursComboBox.getSelectedIndex());
                    int minutess = minutesComboBox.getItemAt(minutesComboBox.getSelectedIndex());
                    int hour_now = currentTime.getHour();
                    int minute_now = currentTime.getMinute();

                    if (hourss == hour_now && minutess == minute_now) {
                        performMaintenance();
                        timeReached.set(true); // Gán giá trị true để dừng vòng lặp
                    }
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private long calculateDelay(int hours, int minutes) {
        long currentMillis = System.currentTimeMillis();
        long scheduledMillis = currentMillis + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000);
        return scheduledMillis - currentMillis;
    }

    private void performMaintenance() {
        Maintenance.gI().start(15);

    }

    public static void runBatchFile(String batchFilePath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", batchFilePath);
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (Exception e) {
        }
    }
}

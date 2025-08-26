/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;

public class CreateMobDataFile {

    public static void createMobDataFile(String imagePath, String txtPath, String outputDataPath, int zoomlv, int mob) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(txtPath));
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputDataPath));

            dos.writeByte(lines.size()); // Số lượng bộ phận

            for (String line : lines) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                int x = Integer.parseInt(parts[1]) * zoomlv;
                int y = Integer.parseInt(parts[2]) * zoomlv;
                int w = Integer.parseInt(parts[3]) * zoomlv;
                int h = Integer.parseInt(parts[4]) * zoomlv;

                dos.writeByte(id);
                dos.writeByte(x);
                dos.writeByte(y);
                dos.writeByte(w);
                dos.writeByte(h);
            }

            // Đọc ảnh và ghi dữ liệu hình ảnh vào tệp
            BufferedImage oriImage = ImageIO.read(new File(imagePath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(oriImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            dos.writeInt(imageBytes.length);
            dos.write(imageBytes);

            dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main() {
        String imagePath = "D/13.png"; // Đường dẫn đến tệp ảnh chứa bộ phận của mob
        String txtPath = "D/data.txt"; // Đường dẫn đến tệp văn bản chứa thông số của mob
        String outputDataPath = "D/mob_data"; // Đường dẫn đến tệp dữ liệu mob đầu ra
        int zoomlv = 2; // Zoom Level
        int mob = 123; // ID của mob

        createMobDataFile(imagePath, txtPath, outputDataPath, zoomlv, mob);
    }
}

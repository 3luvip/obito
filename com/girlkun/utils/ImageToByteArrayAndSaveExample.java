package com.girlkun.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageToByteArrayAndSaveExample {

    public static void main() {
        String text = "Nội dung văn bản của bạn"; // Chuỗi văn bản bạn muốn chuyển đổi
        int id = 1; // ID hoặc tên tệp đầu ra

        try {
            byte[] textData = text.getBytes(); // Chuyển đổi chuỗi văn bản thành mảng byte
            String outputPath = "D:\\CBRO2_1.3_2\\CBRO2_1.3\\CBRO2\\" + id + ".txt"; // Tên tệp đầu ra

            saveByteArrayToFile(textData, outputPath);
            System.out.println("Đã lưu tệp thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] convertImageToByteArray(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    private static void saveByteArrayToFile(byte[] data, String outputPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputPath);
        fos.write(data);
        fos.close();
    }
}

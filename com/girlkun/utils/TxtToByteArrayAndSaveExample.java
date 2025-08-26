package com.girlkun.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TxtToByteArrayAndSaveExample {

    public void convertAndSave(int id) {
        String txtFilePath = "D:\\normal\\effect_data\\58.txt"; // Đường dẫn đến tệp văn bản của bạn

        try {
            byte[] txtData = convertTxtToByteArray(txtFilePath);
            String outputPath = "D:\\normal\\effect_data\\58"; // Tên tệp đầu ra (không có đuôi định dạng)

            saveByteArrayToFile(txtData, outputPath);
            System.out.println("Đã lưu tệp thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] convertTxtToByteArray(String txtFilePath) throws IOException {
        Path path = Paths.get(txtFilePath);
        return Files.readAllBytes(path);
    }

    private static void saveByteArrayToFile(byte[] data, String outputPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputPath);
        fos.write(data);
        fos.close();
    }
}

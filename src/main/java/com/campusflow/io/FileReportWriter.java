package com.campusflow.io;

import java.io.*;

public class FileReportWriter {

    public void writeText(String fileName, String content) throws IOException {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }

    public String readText(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }

        return content.toString();
    }

    public void copyBytes(String source, String destination) throws IOException {
        try (InputStream input = new FileInputStream(source);
             OutputStream output = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
        }
    }
}

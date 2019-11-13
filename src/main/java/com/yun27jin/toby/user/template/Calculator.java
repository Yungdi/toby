package com.yun27jin.toby.user.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int add(String filePath) throws IOException {
        return this.calculateTemplate(filePath, bufferedReader -> {
            int sum = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null)
                sum += Integer.parseInt(line);
            return sum;
        });
    }

    public int multiply(String filePath) throws IOException {
        return this.calculateTemplate(filePath, bufferedReader -> {
            int sum = 1;
            String line;
            while ((line = bufferedReader.readLine()) != null)
                sum *= Integer.parseInt(line);
            return sum;
        });
    }

    private int calculateTemplate(String filePath, BufferedReaderCallback bufferedReaderCallback) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            return bufferedReaderCallback.compute(bufferedReader);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
        }
    }

}

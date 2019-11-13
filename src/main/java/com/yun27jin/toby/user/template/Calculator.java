package com.yun27jin.toby.user.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int add(String filePath) throws IOException {
        return this.readLineTemplate(filePath, 0, (line, res) -> Integer.sum(Integer.parseInt(line), res));
    }

    public int multiply(String filePath) throws IOException {
        return this.readLineTemplate(filePath, 1, (line, res) -> Integer.parseInt(line) * res);
    }

    public String concat(String filePath) throws IOException {
        return this.readLineTemplate(filePath, "", (line, res) -> line + res);
    }


    private <T> T readLineTemplate(String filePath, T initValue, LineCallback<T> lineCallback) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            T res = initValue;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res = lineCallback.compute(line, res);
            }
            return res;
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

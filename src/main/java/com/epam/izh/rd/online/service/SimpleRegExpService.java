package com.epam.izh.rd.online.service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRegExpService implements RegExpService {

    /**
     * Метод должен читать файл sensitive_data.txt (из директории resources) и маскировать в нем конфиденциальную информацию.
     * Номер счета должен содержать только первые 4 и последние 4 цифры (1234 **** **** 5678). Метод должен содержать регулярное
     * выражение для поиска счета.
     *
     * @return обработанный текст
     */
    @Override
    public String maskSensitiveData() {
        StringBuilder result = new StringBuilder();
        URL resource = this.getClass().getResource("/sensitive_data.txt");
        if (resource != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(Paths.get(resource.toURI()).toFile());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                String foundedNumber;
                String replacedNumber;
                Pattern pattern = Pattern.compile("(\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4})");
                while ((line = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        foundedNumber = matcher.group();
                        replacedNumber = foundedNumber.split(" ")[0] + " **** **** " + foundedNumber.split(" ")[3];
                        line = line.replaceAll(foundedNumber, replacedNumber);
                    }
                    result.append(line);
                }
                bufferedReader.close();
                fileInputStream.close();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            return result.toString();
        } else {
            return null;
        }
    }

    /**
     * Метод должен считыввать файл sensitive_data.txt (из директории resources) и заменять плейсхолдер ${payment_amount} и ${balance} на заданные числа. Метод должен
     * содержать регулярное выражение для поиска плейсхолдеров
     *
     * @return обработанный текст
     */
    @Override
    public String replacePlaceholders(double paymentAmount, double balance) {
        StringBuilder result = new StringBuilder();
        URL resource = this.getClass().getResource("/sensitive_data.txt");
        if (resource != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(Paths.get(resource.toURI()).toFile());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                String foundedAmount;
                String foundedBalance;
                Pattern pattern = Pattern.compile(".*(?<amount>\\$\\{payment_amount\\}).*(?<balance>\\$\\{balance\\}).*");
                while ((line = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        foundedAmount = matcher.group("amount");
                        foundedBalance = matcher.group("balance");
                        line = line.replace(foundedAmount, String.format("%.0f", paymentAmount));
                        line = line.replace(foundedBalance, String.format("%.0f", balance));
                    }
                    result.append(line);
                }
                bufferedReader.close();
                fileInputStream.close();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            return result.toString();
        } else {
            return null;
        }
    }
}

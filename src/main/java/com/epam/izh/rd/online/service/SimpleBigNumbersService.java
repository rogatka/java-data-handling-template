package com.epam.izh.rd.online.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class SimpleBigNumbersService implements BigNumbersService {

    /**
     * Метод делит первое число на второе с заданной точностью
     * Например 1/3 с точностью 2 = 0.33
     *
     * @param range точность
     * @return результат
     */
    @Override
    public BigDecimal getPrecisionNumber(int a, int b, int range) {
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b),range,RoundingMode.FLOOR);
    }

    /**
     * Метод находит простое число по номеру
     *
     * @param range номер числа, считая с числа 2
     * @return простое число
     */
    @Override
    public BigInteger getPrimaryNumber(int range) {
        if (range == 0) return BigInteger.valueOf(2);

        BigInteger number = BigInteger.valueOf(3);
        int counterOfPrimaryNumbers = 0;
        while (true) {
            boolean isPrimary = true;      // флаг простого числа
            BigInteger counter = BigInteger.valueOf(3);
//  пока counter <= number/2
            while (number.divide(BigInteger.valueOf(2)).min(counter).equals(counter)) {
//  если number % counter == 0
                if (BigInteger.valueOf(0).equals(number.remainder(counter))) {
                    isPrimary = false;
                    break;
                }
//  увеличиваем счетчик на 2
                counter = counter.add(BigInteger.valueOf(2));
            }
//  если число - простое, то увеличиваем счетчик простых чисел
            if (isPrimary) {
                counterOfPrimaryNumbers++;
            }
//  если счетчик простых чисел стал равным range, то возвращаем искомое простое число
            if (counterOfPrimaryNumbers == range) return number;
//  иначе переходим к следующему числу
            number = number.add(BigInteger.valueOf(2));
        }
    }
}
package com.orderflow.algorithm;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    public static int[] bruteForce(int[] numbers, int target) {
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[] {i, j};
                }
            }
        }

        return new int[0];
    }

    public static int[] optimized(int[] numbers, int target) {
        Map<Integer, Integer> numbersMap = new HashMap<>();

        for (int i = 0; i < numbers.length; i++) {
            int complement = target - numbers[i];
            Integer previousIndex = numbersMap.get(complement);

            if (previousIndex != null) {
                return new int[] {previousIndex, i};
            }

            numbersMap.put(numbers[i], i);
        }

        return new int[0];
    }
}
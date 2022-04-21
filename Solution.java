package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class Solution {
    public static void main(String[] args) {
        testStrategy(new HashMapStorageStrategy(), 10000);
        testStrategy(new OurHashMapStorageStrategy(), 10000);
        testStrategy(new FileStorageStrategy(), 10);
        testStrategy(new OurHashBiMapStorageStrategy(),10000);
        testStrategy(new HashBiMapStorageStrategy(), 10000);
        testStrategy(new DualHashBidiMapStorageStrategy(), 10000);
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber){
        Helper.printMessage(strategy.getClass().getSimpleName());
        Set<String> set = new HashSet<>();
        for (int i = 0; i < elementsNumber; ++i) {
            set.add(Helper.generateRandomString());
        }
        Shortener shortener = new Shortener(strategy);

        Date date1 = new Date();
        Set<Long> set2 = getIds(shortener, set);
        Helper.printMessage(String.valueOf(new Date().getTime() - date1.getTime()));

        Date date2 = new Date();
        Set<String> setStrings = getStrings(shortener, set2);
        Helper.printMessage(String.valueOf(new Date().getTime() - date2.getTime()));



        if (set.size() == setStrings.size()){
            Helper.printMessage("Тест пройден.");
        }else {
            Helper.printMessage("Тест не пройден.");
        }
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings){
        Set<Long> set = new HashSet<>();
        for (String s : strings) {
            set.add(shortener.getId(s));
        }
        return set;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys){
        Set<String> set = new HashSet<>();
        for (Long l : keys) {
            set.add(shortener.getString(l));
        }
        return set;
    }

}

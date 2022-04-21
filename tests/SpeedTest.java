package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {
    @Test
    public void testHashMapStorage(){
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());
        Set<String> origStrings = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }
        Set<Long> ids1 = new HashSet<>();
        Set<String> strings1 = new HashSet<>();
                
        long timeToGetIds1 = getTimeToGetIds(shortener1, origStrings, ids1);
        long timeToGetStrings1 = getTimeToGetStrings(shortener1, ids1, strings1);

        Set<Long> ids2 = new HashSet<>();
        Set<String> strings2 = new HashSet<>();

        long timeToGetIds2 = getTimeToGetIds(shortener2, origStrings, ids2);
        long timeToGetStrings2 = getTimeToGetStrings(shortener2, ids2, strings2);
        
        Assert.assertTrue(timeToGetIds1 > timeToGetIds2);
        Assert.assertEquals(timeToGetStrings1, timeToGetStrings2, 30);
    }
    
    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids){
        Date date = new Date();
        for (String s:strings) {
            ids.add(shortener.getId(s));
        }
        return new Date().getTime() - date.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        Date date = new Date();
        for (Long l : ids) {
            strings.add(shortener.getString(l));
        }
        return new Date().getTime() - date.getTime();
    }


}

package com.javarush.task.task33.task3310.strategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBucket {
    private Path path;

    public FileBucket() {
        try {
            path = Files.createTempFile(Integer.toHexString(hashCode()), ".tmp");
            path.toFile().deleteOnExit();

            Files.deleteIfExists(path);
            Files.createFile(path);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getFileSize(){
        long size = 0L;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void putEntry(Entry entry){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))){
            objectOutputStream.writeObject(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Entry getEntry(){
        if (getFileSize() > 0){
            try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(path))){
                return (Entry) inputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void remove(){
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

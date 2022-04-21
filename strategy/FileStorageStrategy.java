package com.javarush.task.task33.task3310.strategy;

public class FileStorageStrategy implements StorageStrategy{
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000;

    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    int size;
    FileBucket[] table;
    long maxBucketSize;

    public FileStorageStrategy() {
        table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
        for (int i = 0; i < table.length; i++) {
            table[i] = new FileBucket();
        }
    }

    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }

    int indexFor(int hash, int length){
        return hash & (length -1);
    }

    final Entry getEntry(Long key){
        if (size == 0){
            return null;
        }
        int hash = key.hashCode();
        int index = indexFor(hash, table.length);
        for (Entry e = table[index].getEntry(); e != null; e = e.next) {
            if (key.equals(e.key)){
                return e;
            }
        }
        return null;
    }

    void resize(int newCapacity){
        FileBucket[] newTable = new FileBucket[newCapacity];
        for (int i = 0; i < newTable.length; i++) {
            newTable[i] = new FileBucket();
        }
        transfer(newTable);

        for (int i = 0; i < table.length; i++) {
            table[i].remove();
        }

        table = newTable;

    }

    void transfer(FileBucket[] newTable){
        int newCapacity = newTable.length;
        maxBucketSize = 0;
        for (FileBucket f : table) {
            Entry e= f.getEntry();
            while (e != null) {
                Entry next = e.next;
                int indexInNewTable = indexFor(e.hashCode(), newCapacity);
                e.next = newTable[indexInNewTable].getEntry();
                newTable[indexInNewTable].putEntry(e);
                e = next;
            }
            long currentBucketSize = f.getFileSize();
            if (currentBucketSize > maxBucketSize){
                maxBucketSize = currentBucketSize;
            }
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex){
        if ((maxBucketSize >= bucketSizeLimit)) {
            resize(2 * table.length);
            bucketIndex = indexFor(key.hashCode(), table.length);
        }

        createEntry(hash, key, value, bucketIndex);
    }


    void createEntry(int hash, Long key, String value, int bucketIndex){
        Entry e = table[bucketIndex].getEntry();
        table[bucketIndex].putEntry(new Entry(hash, key, value, e));
        size++;

        long currentBucketSize = table[bucketIndex].getFileSize();
        if (currentBucketSize > maxBucketSize){
            maxBucketSize = currentBucketSize;
        }
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (FileBucket f:table) {
            for (Entry e = f.getEntry(); e != null; e = e.next)
                if (value.equals(e.value))
                    return true;
        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = key.hashCode();
        int index = indexFor(hash, table.length);
        for (Entry entry = table[index].getEntry(); entry != null; entry = entry.next){
            if (key.equals(entry.key)){
                entry.value = value;
                return;
            }
        }
        addEntry(hash, key, value, index);
    }

    @Override
    public Long getKey(String value) {
        for (FileBucket f:table) {
            for (Entry e = f.getEntry(); e != null; e = e.next)
                if (value.equals(e.value))
                    return e.getKey();
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        Entry entry = getEntry(key);
        if (entry != null){
            return entry.getValue();
        }
        return null;

    }
}

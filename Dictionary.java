import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary<Key, Value> {
    private SeparateChainingHashST<Key, Value> sch;
    private LinearProbingHashST<Key, Value> lph;

    // Constructor for specifying hash table type
    public Dictionary(int capacity, boolean useEarlyHash, boolean use_sch){
        if (use_sch){
            sch = new SeparateChainingHashST<>(capacity, useEarlyHash);
            lph = null;
        }
        else {
            lph = new LinearProbingHashST<>(capacity, useEarlyHash);
            sch = null;
        }
    }

    // Default Constructor, SeparateChainingHashTable that uses earlyHashCode()
    public Dictionary(int capacity, boolean useEarlyHash){
        this(capacity, useEarlyHash, true);
    }

    public void put(Key key, Value value){
        if (sch != null){
            sch.put(key, value);
        }
        else if (lph != null){
            lph.put(key, value);
        }
        else {
            throw new IllegalArgumentException("Hash table not initialized");
        }
    }

    public void loadDictionary(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String word;
        int lineNumber = 1;

        while((word = br.readLine()) != null){
            if (sch != null){
                sch.put((Key) word.toLowerCase(), (Value) Integer.valueOf(lineNumber));
            }
            else if (lph != null){
                lph.put((Key) word.toLowerCase(), (Value) Integer.valueOf(lineNumber));
            }
            lineNumber++;
        }
        br.close();
    }

    public boolean contains(String word){
        if (sch != null){
            return sch.contains((Key) word.toLowerCase());
        }
        else if (lph != null){
            return lph.contains((Key) word.toLowerCase());
        }
        return false;
    }

    public int getComparisons(boolean useEarlyHash) {
        if (sch != null){
            return sch.getComparisons();
        }
        else if (lph != null){
            return lph.getComparisons();
        }
        return 0;
    }

    public void resetComparisons() {
        if (sch != null){
            sch.resetComparisons();
        }
        if (lph != null){
            lph.resetComparisons();
        }
    }

}

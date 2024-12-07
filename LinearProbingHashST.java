
public class LinearProbingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int n;
    private int m;
    private Key[] keys;
    private Value[] values;
    private boolean useEarlyHash;
    private int comparisons;

    public LinearProbingHashST() {
        this(INIT_CAPACITY, false);
    }

    public LinearProbingHashST(int capacity, boolean useEarlyHash) {
        m = capacity;
        n = 0;
        keys = (Key[]) new Object[m];
        values = (Value[]) new Object[m];
        this.useEarlyHash = useEarlyHash;
        this.comparisons = 0;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    public void setUseEarlyHash(boolean useEarlyHash) {
        this.useEarlyHash = useEarlyHash;
    }

    private int hash(Key key) {
        if (useEarlyHash) {
            return (earlyHashCode(key));
        }
        else {
            return (currentHashCode(key));
        }
    }

    private int earlyHashCode(Key key) {
        String s = key.toString();
        int hash = 0;
        int skip = Math.max(1, s.length() / 8);
        for (int i = 0; i < s.length(); i += skip)
            hash = (hash * 37) + s.charAt(i);
        return (hash & 0x7fffffff) % m;
    }

    private int currentHashCode(Key key){
        String s = key.toString();
        int hash = 0;
        for (int i = 0; i < s.length(); i++)
            hash = (hash * 31) + s.charAt(i);
        return (hash & 0x7fffffff) % m;
    }

    private void resize(int capacity, boolean useEarlyHash) {
        capacity = Math.max(capacity, INIT_CAPACITY);
        if (capacity < m) return;
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<>(capacity, useEarlyHash);
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], values[i]);
            }
        }
        keys = temp.keys;
        values = temp.values;
        m = temp.m;
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("argument to put() is null");

        if (value == null) {
            delete(key);
            return;
        }

        if (n >= m/2) resize(2 * m, useEarlyHash);

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
            comparisons++;
            if (keys[i].equals(key)) {
                values[i] = value;
                return;
            }
        }
        keys[i] = key;
        values[i] = value;
        n++;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (int i = hash(key); keys[i] != null; i = (i + 1) % m) {
            comparisons++;
            if (keys[i].equals(key))
                return values[i];
        }
        return null;
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % m;
        }

        keys[i] = null;
        values[i] = null;

        i = (i + 1) % m;
        while (keys[i] != null) {
            Key keyToRehash = keys[i];
            Value valueToRehash = values[i];
            keys[i] = null;
            values[i] = null;
            n--;
            put(keyToRehash, valueToRehash);
            i = (i + 1) % m;
        }

        n--;

        if (n > 0 && n <= m/8) resize(m/2, useEarlyHash);

        assert check();
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < m; i++)
            if (keys[i] != null) queue.enqueue(keys[i]);
        return queue;
    }

    private boolean check() {
        if (m < 2*n) {
            System.err.println("Hash table size m = " + m + "; array size n = " + n);
            return false;
        }

        for (int i = 0; i < m; i++) {
            if (keys[i] == null) continue;
            else if (get(keys[i]) != values[i]) {
                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; values[i] = " + values[i]);
                return false;
            }
        }
        return true;
    }

    public int getComparisons() {
        return comparisons;
    }

    public void resetComparisons() {
        comparisons = 0;
    }

}

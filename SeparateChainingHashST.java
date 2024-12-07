
public class SeparateChainingHashST<Key, Value>{
    private static final int INIT_CAPACITY = 4;
    private boolean useEarlyHash;
    private int n;
    private int m;
    private SequentialSearchST<Key, Value>[] st;
    private int comparisons = 0;

    public SeparateChainingHashST(int m, boolean useEarlyHash) {
        this.m = m;
        this.useEarlyHash = useEarlyHash;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
        for (int i = 0; i < m; i++) {
            st[i] = new SequentialSearchST<>();
        }
    }

    public void setUseEarlyHash(boolean useEarlyHash) {
        this.useEarlyHash = useEarlyHash;
    }

    private void resize(int chains, boolean useEarlyHash) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<>(chains, useEarlyHash);
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                if (key != null) {
                    temp.put(key, st[i].get(key));
                }
            }
        }
        this.m = temp.m;
        this.n = temp.n;
        this.st = temp.st;
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

    public int size(){
        return n;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public boolean contains(Key key){
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    public Value get(Key key){
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash(key);

        if (st[i] == null) return null;

        for (Key k: st[i].keys()) {
            comparisons++;
            if (k != null && k.equals(key)) return st[i].get(k);
        }

        return null;
    }

    public void put(Key key, Value val){
        if (key == null) throw new IllegalArgumentException("argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }
        if (n >= 10*m) resize(2 * m, useEarlyHash);
        int i = hash(key);
        comparisons++;
        if (!st[i].contains(key)) n++;
        st[i].put(key, val);
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        int i = hash(key);
        comparisons++;
        if (!st[i].contains(key)) n--;
        st[i].delete(key);

        if (m > INIT_CAPACITY && n <= 2*m) resize(m/2, useEarlyHash);
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    }

    public int getComparisons(){
        return comparisons;
    }

    public void resetComparisons() {
        comparisons = 0;
    }

}

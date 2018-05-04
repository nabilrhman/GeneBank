import java.util.Iterator;
import java.util.LinkedList;

public class BTreeCache implements Iterable<BTreeNode>
{

    private final int MAX_SIZE;
    private int numHits, numMisses;

    private LinkedList<BTreeNode> list;

    /**
     * Creates a new BTreeCache with the specified capacity.
     *
     * @param MAX_SIZE max capacity of the cache
     */
    public BTreeCache(int MAX_SIZE)
    {
        this.MAX_SIZE = MAX_SIZE;
        list = new LinkedList<BTreeNode>();
    }

    /**
     * Adds a node to the BTreeCache.
     * If this causes a node to fall off the list,
     * that cache node is returned; otherwise null is returned.
     *
     * @param nodeToAdd The BTreeNode to add
     * @param nodeToAdd The offset of the node in the file
     */
    public BTreeNode add(BTreeNode nodeToAdd, int offset)
    {
        BTreeNode nodeToReturn = null;
        if (isFull())
            nodeToReturn = list.removeLast();
        list.addFirst(nodeToAdd);
        return nodeToReturn;
    }

    /**
     * Removes all the elements from the BTreeCache.
     */
    public void clearCache()
    {
        list.clear();
    }

    /**
     * Reads a node from the cache.
     * If it is cached; return false otherwise.
     * If the node is found it is automatically moved to the beginning of the
     * cache.
     *
     * @param offset Offset of the node to read
     * @return The node at offset, or null if the node is not in the cache
     */
    public BTreeNode readNode(int offset)
    {
        for (BTreeNode n : list)
        {
            // FIXME
            if (n.getOffset() == offset)
            {
                list.remove(n);
                list.addFirst(n);
                increaseNumHits();
                return n;
            }
        }
        // Goes through the whole list without finding it
        increaseNumMisses();
        return null;
    }

    /**
     * Gets the number of reads performed on this BTreeCache.
     *
     * @return Number of reads
     */
    public int getNumReferences()
    {
        return numHits + numMisses;
    }

    private void increaseNumHits()
    {
        numHits++;
    }

    private void increaseNumMisses()
    {
        numMisses++;
    }

    /**
     * Gets the number of cache hits.
     *
     * @return Number of cache hits
     */
    public int getNumHits()
    {
        return numHits;
    }

    /**
     * Gets the number of cache misses.
     *
     * @return Number of cache misses
     */
    public int getNumMisses()
    {
        return numMisses;
    }

    /**
     * Gets the hit ratio for the cache.
     *
     * @return Number of cache hits / number of cache reads
     */
    public double getHitRatio()
    {
        double ratio = ((double) getNumHits()) / getNumReferences();
        return ratio;
    }

    /**
     * Get the number of elements stored in this cache.
     *
     * @return Number of elements
     */
    public int getSize()
    {
        return list.size();
    }

    /**
     * Get whether this cache is full.
     *
     * @return True if the cache is full, otherwise false
     */
    public boolean isFull()
    {
        return getSize() == MAX_SIZE;
    }

    @Override
    public Iterator<BTreeNode> iterator()
    {
        return list.iterator();
    }
}                                                                                                                                                                     

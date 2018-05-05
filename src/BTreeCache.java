import java.util.Iterator;
import java.util.LinkedList;

/**
 * The cache class used by BTree
 *
 * @autor nabilrhman
 */
public class BTreeCache implements Iterable<BTreeNode>
{

    private final int MAX_SIZE;
    private int numHits, numMisses;
    private LinkedList<BTreeNode> list;

    public BTreeCache(int MAX_SIZE)
    {
        this.MAX_SIZE = MAX_SIZE;
        list = new LinkedList<BTreeNode>();
    }

    public BTreeNode add(BTreeNode nodeToAdd, int offset)
    {
        BTreeNode nodeToReturn = null;
        if (isFull())
            nodeToReturn = list.removeLast();
        list.addFirst(nodeToAdd);
        return nodeToReturn;
    }

    public void clearCache()
    {
        list.clear();
    }

    public BTreeNode readNode(int offset)
    {
        for (BTreeNode n : list)
        {
            if (n.getOffset() == offset)
            {
                list.remove(n);
                list.addFirst(n);
                increaseNumHits();
                return n;
            }
        }
        increaseNumMisses();
        return null;
    }

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

    public int getNumHits()
    {
        return numHits;
    }

    public int getNumMisses()
    {
        return numMisses;
    }

    public double getHitRatio()
    {
        double ratio = ((double) getNumHits()) / getNumReferences();
        return ratio;
    }

    public int getSize()
    {
        return list.size();
    }

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

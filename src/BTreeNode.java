import java.util.LinkedList;
/**
 * The node class used by BTree
 *
 * @autor nabilrhman
 */
public class BTreeNode
{

    private int n;
    private LinkedList<TreeObject> keys;
    private LinkedList<Integer> children;
    private boolean isLeaf;
    private int parent;
    private int offset;

    public BTreeNode()
    {
        parent = -1;
        keys = new LinkedList<TreeObject>();
        children = new LinkedList<Integer>();
        n = 0;
    }

    public int getN()
    {
        return n;
    }

    public void setParent(int parent)
    {
        this.parent = parent;
    }

    public int getParent()
    {
        return parent;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setN(int value)
    {
        n = value;
    }

    public void addChild(int child)
    {
        children.add(child);
    }

    public int removeChild(int index)
    {
        return children.remove(index);
    }

    public int getChild(int index)
    {
        return children.get(index).intValue();
    }

    public TreeObject removeKey(int index)
    {
        return keys.remove(index);
    }

    public void addKey(TreeObject element)
    {
        keys.add(element);
    }

    public TreeObject getKey(int key)
    {
        TreeObject element = keys.get(key);
        return element;
    }

    public void setIsLeaf(boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public boolean isLeaf()
    {
        return isLeaf;
    }

    public void addKey(TreeObject element, int index)
    {
        keys.add(index, element);
    }

    public void addChild(Integer child, int index)
    {
        children.add(index, child);
    }

    public LinkedList<TreeObject> getKeys()
    {
        return keys;
    }

    @Override
    public String toString()
    {
        String s = new String();
        s += "keys: ";
        for (int i = 0; i < keys.size(); i++)
        {
            s += (keys.get(i) + " ");
        }
        s += "\nchildren: ";
        for (int i = 0; i < children.size(); i++)
        {
            s += (children.get(i) + " ");
        }
        return s;
    }

    public LinkedList<Integer> getChildren()
    {
        return children;
    }
}

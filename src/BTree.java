import java.io.*;

/**
 * Implementation of B-Tree
 *
 * @author nabilrhman
 */
public class BTree
{
    private int degree;
    private BTreeNode root;
    private RandomAccessFile randomAccessFile;
    private File file;
    private int placeToInsert;
    private int rootOffset;
    private int bTreeNodeSize;
    private BTreeCache cache;

    public BTree(int degree, String fileName, boolean useCache, int cacheSize)
    {
        bTreeNodeSize = 32 * degree - 3;
        rootOffset = 12;
        placeToInsert = rootOffset + bTreeNodeSize;

        this.degree = degree;
        if (useCache)
            cache = new BTreeCache(cacheSize);

        BTreeNode x = new BTreeNode();
        root = x;
        root.setOffset(rootOffset);
        x.setIsLeaf(true);
        x.setN(0);

        try
        {
            file = new File(fileName);
            file.delete();
            file.createNewFile();
            randomAccessFile = new RandomAccessFile(file, "rw");
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println("Error: File is corrupt or missing.");
            System.exit(-1);
        }
        catch (IOException ioe)
        {
            System.err.println("Error: IO Exception occurred.");
            System.exit(-1);
        }

        writeTreeMetadata();
    }

    public BTree(int degree, File fileName, boolean useCache, int cacheSize)
    {

        try
        {
            randomAccessFile = new RandomAccessFile(fileName, "r");
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println("Error: File is corrupt or missing.");
            System.exit(-1);
        }

        readTreeMetadata();
        root = readNode(rootOffset);
    }

    public void insert(long k)
    {
        BTreeNode r = this.root;
        int i = r.getN();
        if (i == (2 * degree - 1))
        {
            TreeObject obj = new TreeObject(k);
            while (i > 0 && obj.compareTo(r.getKey(i - 1)) < 0)
            {
                i--;
            }
            if (i < r.getN())
            {
            }
            if (i > 0 && obj.compareTo(r.getKey(i - 1)) == 0)
                r.getKey(i - 1).increaseFrequency();
            else
            {
                BTreeNode s = new BTreeNode();
                s.setOffset(r.getOffset());
                this.root = s;
                r.setOffset(placeToInsert);
                r.setParent(s.getOffset());
                s.setIsLeaf(false);
                s.addChild(r.getOffset());
                splitChild(s, 0, r);
                insertNonfull(s, k);
            }
        }
        else
            insertNonfull(r, k);
    }

    public void insertNonfull(BTreeNode x, long k)
    {
        int i = x.getN();
        TreeObject treeObject = new TreeObject(k);
        if (x.isLeaf())
        {
            if (x.getN() != 0)
            {
                while (i > 0 && treeObject.compareTo(x.getKey(i - 1)) < 0)
                {
                    i--;
                }
            }
            if (i > 0 && treeObject.compareTo(x.getKey(i - 1)) == 0)
            {
                x.getKey(i - 1).increaseFrequency();
            }
            else
            {
                x.addKey(treeObject, i);
                x.setN(x.getN() + 1);
            }
            writeNode(x, x.getOffset());
        }
        else
        {
            while (i > 0 && (treeObject.compareTo(x.getKey(i - 1)) < 0))
            {
                i--;
            }
            if (i > 0 && treeObject.compareTo(x.getKey(i - 1)) == 0)
            {
                x.getKey(i - 1).increaseFrequency();
                writeNode(x, x.getOffset());
                return;
            }
            int offset = x.getChild(i);
            BTreeNode y = readNode(offset);
            if (y.getN() == 2 * degree - 1)
            {
                int j = y.getN();
                while (j > 0 && treeObject.compareTo(y.getKey(j - 1)) < 0)
                {
                    j--;
                }
                if (j > 0 && treeObject.compareTo(y.getKey(j - 1)) == 0)
                {
                    y.getKey(j - 1).increaseFrequency();
                    writeNode(y, y.getOffset());
                    return;
                }
                else
                {
                    splitChild(x, i, y);
                    if (treeObject.compareTo(x.getKey(i)) > 0)
                        i++;
                }
            }
            offset = x.getChild(i);
            BTreeNode child = readNode(offset);
            insertNonfull(child, k);
        }
    }

    public void splitChild(BTreeNode x, int i, BTreeNode y)
    {
        BTreeNode z = new BTreeNode();
        z.setIsLeaf(y.isLeaf());
        z.setParent(y.getParent());
        for (int j = 0; j < degree - 1; j++)
        {
            z.addKey(y.removeKey(degree));
            z.setN(z.getN() + 1);
            y.setN(y.getN() - 1);
        }

        if (!y.isLeaf())
        {
            for (int j = 0; j < degree; j++)
            {
                z.addChild(y.removeChild(degree));
            }
        }

        x.addKey(y.removeKey(degree - 1), i);
        x.setN(x.getN() + 1);
        y.setN(y.getN() - 1);

        if (x == root && x.getN() == 1)
        {
            writeNode(y, placeToInsert);
            placeToInsert += bTreeNodeSize;
            z.setOffset(placeToInsert);
            x.addChild(z.getOffset(), i + 1);
            writeNode(z, placeToInsert);
            writeNode(x, rootOffset);
            placeToInsert += bTreeNodeSize;
        }
        else
        {
            writeNode(y, y.getOffset());
            z.setOffset(placeToInsert);
            writeNode(z, placeToInsert);
            x.addChild(z.getOffset(), i + 1);
            writeNode(x, x.getOffset());
            placeToInsert += bTreeNodeSize;
        }
    }

    public TreeObject search(BTreeNode x, long k)
    {
        int i = 0;
        TreeObject obj = new TreeObject(k);
        while (i < x.getN() && (obj.compareTo(x.getKey(i)) > 0))
        {
            i++;
        }
        if (i < x.getN() && obj.compareTo(x.getKey(i)) == 0)
        {
            return x.getKey(i);
        }
        if (x.isLeaf())
        {
            return null;
        }
        else
        {
            int offset = x.getChild(i);
            BTreeNode y = readNode(offset);
            return search(y, k);
        }
    }

    public BTreeNode getRoot()
    {
        return root;
    }

    public void inOrderPrint(BTreeNode n)
    {
        System.out.println(n);
        if (n.isLeaf() == true)
        {
            for (int i = 0; i < n.getN(); i++)
            {
                System.out.println(n.getKey(i));
            }
            return;
        }
        for (int i = 0; i < n.getN() + 1; ++i)
        {
            int offset = n.getChild(i);
            BTreeNode y = readNode(offset);
            inOrderPrint(y);
            if (i < n.getN())
                System.out.println(n.getKey(i));
        }
    }

    public void inOrderPrintToWriter(BTreeNode node, PrintWriter writer, int sequenceLength) throws IOException
    {
        GeneBankConvert gbc = new GeneBankConvert();
        for (int i = 0; i < node.getN(); i++)
        {
            writer.print(node.getKey(i).getFrequency() + " ");
            writer.println(gbc.convertLongToString(node.getKey(i).getData(), sequenceLength));
        }
        if (!node.isLeaf())
        {
            for (int i = 0; i < node.getN() + 1; ++i)
            {
                int offset = node.getChild(i);
                BTreeNode y = readNode(offset);
                inOrderPrintToWriter(y, writer, sequenceLength);

                if (i < node.getN())
                {
                    writer.print(node.getKey(i).getFrequency() + " ");
                    writer.println(gbc.convertLongToString(node.getKey(i).getData(), sequenceLength));
                }
            }
        }
    }

    public void writeNode(BTreeNode n, int offset)
    {
        if (cache != null)
        {
            BTreeNode cnode = cache.add(n, offset);
            // if a node was pushed off, write it
            if (cnode != null) writeNodeToFile(cnode, cnode.getOffset());
        }
        else
        {
            writeNodeToFile(n, offset);
        }
    }

    private void writeNodeToFile(BTreeNode n, int offset)
    {
        int i = 0;
        try
        {
            writeNodeMetadata(n, n.getOffset());
            randomAccessFile.writeInt(n.getParent());
            for (i = 0; i < 2 * degree - 1; i++)
            {
                if (i < n.getN() + 1 && !n.isLeaf())
                {
                    randomAccessFile.writeInt(n.getChild(i));
                }
                else if (i >= n.getN() + 1 || n.isLeaf())
                {
                    randomAccessFile.writeInt(0);
                }
                if (i < n.getN())
                {
                    long data = n.getKey(i).getData();
                    randomAccessFile.writeLong(data);
                    int frequency = n.getKey(i).getFrequency();
                    randomAccessFile.writeInt(frequency);
                }
                else if (i >= n.getN() || n.isLeaf())
                {
                    randomAccessFile.writeLong(0);
                }
            }
            if (i == n.getN() && !n.isLeaf())
            {
                randomAccessFile.writeInt(n.getChild(i));
            }
        }
        catch (IOException ioe)
        {
            System.err.println("Error: IO Exception occurred!");
            System.exit(-1);
        }
    }

    public BTreeNode readNode(int offset)
    {

        BTreeNode y = null;

        // if node is cached, we can just read it from there
        if (cache != null) y = cache.readNode(offset);
        if (y != null) return y;

        y = new BTreeNode();
        TreeObject obj = null;
        y.setOffset(offset);
        int k = 0;
        try
        {
            randomAccessFile.seek(offset);
            boolean isLeaf = randomAccessFile.readBoolean();
            y.setIsLeaf(isLeaf);
            int n = randomAccessFile.readInt();
            y.setN(n);
            int parent = randomAccessFile.readInt();
            y.setParent(parent);
            for (k = 0; k < 2 * degree - 1; k++)
            {
                if (k < y.getN() + 1 && !y.isLeaf())
                {
                    int child = randomAccessFile.readInt();
                    y.addChild(child);
                }
                else if (k >= y.getN() + 1 || y.isLeaf())
                {
                    randomAccessFile.seek(randomAccessFile.getFilePointer() + 4);
                }
                if (k < y.getN())
                {
                    long value = randomAccessFile.readLong();
                    int frequency = randomAccessFile.readInt();
                    obj = new TreeObject(value, frequency);
                    y.addKey(obj);
                }
            }
            if (k == y.getN() && !y.isLeaf())
            {
                int child = randomAccessFile.readInt();
                y.addChild(child);
            }
        }
        catch (IOException ioe)
        {
            System.err.println(ioe.getMessage());
            System.exit(-1);
        }

        return y;
    }

    public void writeTreeMetadata()
    {
        try
        {
            randomAccessFile.seek(0);
            randomAccessFile.writeInt(degree);
            randomAccessFile.writeInt(32 * degree - 3);
            randomAccessFile.writeInt(12);
        }
        catch (IOException ioe)
        {
            System.err.println("Error: IO Exception occurred.");
            System.exit(-1);
        }
    }

    public void readTreeMetadata()
    {
        try
        {
            randomAccessFile.seek(0);
            degree = randomAccessFile.readInt();
            bTreeNodeSize = randomAccessFile.readInt();
            rootOffset = randomAccessFile.readInt();
        }
        catch (IOException ioe)
        {
            System.err.println("Error: IO Exception occurred.");
            System.exit(-1);
        }
    }

    public void writeNodeMetadata(BTreeNode x, int offset)
    {
        try
        {
            randomAccessFile.seek(offset);
            randomAccessFile.writeBoolean(x.isLeaf());
            randomAccessFile.writeInt(x.getN());
        }
        catch (IOException ioe)
        {
            System.err.println("Error: IO Exception occurred.");
            System.exit(-1);
        }
    }

    public void flushCache()
    {
        if (cache != null)
        {
            for (BTreeNode cnode : cache)
                writeNodeToFile(cnode, cnode.getOffset());
        }
    }
}

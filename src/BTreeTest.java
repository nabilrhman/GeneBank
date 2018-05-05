import java.util.Random;
/**
 * Tests for the BTree class
 *
 * @autor nabilrhman
 */
public class BTreeTest
{
    private static Random randomNumGenerator;

    public static void main(String[] args)
    {
        BTree bTree = new BTree(128, "test1", true, 100);
        randomNumGenerator = new Random();
        for (int i = 1; i < 1000; i++)
        {
            bTree.insert(i);
            bTree.inOrderPrint(bTree.getRoot());
            System.out.println();
            bTree.insert(i);
            bTree.inOrderPrint(bTree.getRoot());
            System.out.println();
        }
        for (int i = 1; i < 1000; i++)
        {
            TreeObject a = bTree.search(bTree.getRoot(), i);
            if (a == null || a.compareTo(new TreeObject(i)) != 0)
                System.err.println("Search failed.");
        }
        long[] keys = new long[100];
        bTree = new BTree(2, "test2", true, 100);
        for (int i = 0; i < 100; i++)
        {
            int num = randomNumGenerator.nextInt(15);
            bTree.insert(num);
            keys[i] = num;
            bTree.inOrderPrint(bTree.getRoot());
            System.out.println();
            bTree.insert(num);
            bTree.inOrderPrint(bTree.getRoot());
            System.out.println();
        }
        for (int i = 0; i < 100; i++)
        {

            TreeObject a = bTree.search(bTree.getRoot(), keys[i]);
            if (a == null || a.compareTo(new TreeObject(keys[i])) != 0)
                System.err.println("Search failed.");
        }
    }
}

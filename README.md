
# Project - GeneBank

 - Austin Foy, Maruf Ahmed, Nabil Rahman 
 - CS 321
 - Instructor: Steven Cutchin 
 - Spring 2018, Boise State University

## Included files

#### BTree.java
Implementation of B-Tree
#### BTreeCache.java
The cache class used by BTree.
#### BTreeCacheNode.java
The cache class used by BTreeCache.
#### BTreeNode.java
The node class used by BTree.
#### BTreeTest.java
Tests for the BTree class.
#### GeneBankConvert.java
Class that converts between String and long integer representation of gene sequences.
#### GeneBankCreateBTree.java
The driver class for the GeneBankCreateBTree program that creates a BTree from a user-specified .gbk file
#### GeneBankSearch.java
The driver class for the GeneBankSearch program that searches a BTree file created by GeneBankCreateBTree for sequences specified in a query file.
#### Parser.java
Alternative parser class that converts between String and long integer representation of gene sequences.
#### TreeObject.java
The object class used by BTree.
  
## Compiling and running

### Compiling
```
javac GeneBankCreateBTree.java
javac GeneBankSearch.java
```

### Running GeneBankCreateBTree

Exucute the following command.
```
java GeneBankCreateBTree <cache> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]
```
```
<cache>: 1 to use a cache, 0 to use no cache.
<degree>: Degree to use for the BTree. If 0, the default degree (optimal for block size 4096) is used.
<gbk file>: .gbk file to create a BTree from.
<sequence length>: Length of gene sequences to store in the BTree.
<cache size>: Size of the cache (maximum number of nodes to store).
<debug level>: 1 to write a dump of the BTree data to a file named "dump". 0 to not write a dump.
```
The GeneBankCreateBTree program will create a BTree file in the same directory as the gbk file with the following format.
```
<gbk filename>.btree.data.<sequence length>.<degree>
```

### Running GeneBankSearch

To run GeneBankSearch, the user need to have a finished BTree file created by the GeneBankCreateBTree program. The sequence length in the BTree should be exactly the same as the sequence length that is being searched for.

Execute the following command.
```
java GeneBankSearch <cache> <btree file> <query file> [<cache size>] [<debug level>]
```
```
<cache>: 1 to use a cache, 0 to use no cache.
<btree file>: Filename of the BTree file to search.
<query file>: Filename of the query file to use.
<cache size>: Size of the cache (maximum number of nodes to store).
<debug level>: If included, must be an integer. Currently has no effect.
```
GeneBankSearch will then print a summary of the search results showing how many times each sequence was found.

## Timing results
  
Tested using Maruf's laptop.

### GeneBankCreateBTree

```
$ time java GeneBankCreateBTree 0 0 data/test3.gbk 7
real    4m56.066s
user    0m0.015s
sys     0m0.000s
$ time java GeneBankCreateBTree 1 0 data/test3.gbk 7 100
real    2m21.542s
user    0m0.000s
sys     0m0.015s
$ time java GeneBankCreateBTree 1 0 data/test3.gbk 7 500
real    2m2.611s
user    0m0.015s
sys     0m0.015s
```
The cache is a significant improvement over no cache, but size 500 isn't much better than 100.

### GeneBankSearch

```
$ time java GeneBankSearch 0 data/test3.gbk.btree.data.7.128 queries/query7 > /dev/null
real        0m23.219s
user        0m10.074s
sys         0m13.413s

$ time java GeneBankSearch 1 data/test3.gbk.btree.data.7.128 queries/query7 100 > /dev/null
real        0m23.096s
user        0m10.309s
sys         0m13.270s

$ time java GeneBankSearch 1 data/test3.gbk.btree.data.7.128 queries/query7 500 > asdf
real        0m23.646s
user        0m10.505s
sys         0m13.604s
```
The cache does not appear to be a very significant improvement here, but this makes sense considering GeneBankSearch, unlike GeneBankCreateBTree, does not perform any writes.

## Discussion

### Concept
 
The Concept of this project was to apply a BTree to a real world application which in our case was a Gene file that would input specific characters into structures to form protein combinations. The goal was to take the sequence of characters and convert them to a key which would then be stored into a ROM that could be called upon later. And, for a user to search through the BTree to find any combinations of keys that could match a protein sequence that the user inputted.

### Explanation of the BTree file format:

First, the BTree writes its metadata to the disk. This metadata is the degree of the BTree, the size of the BTreeNode, and the offset of the root node. 
There is a variable in the BTree that keeps track of where to insert which increments each time a node is inserted. When each node is written, the BTree writes the node's metadata first. The node's metadata is whether or not the node is a leaf, and its number of keys. Each BTreeNode holds on to a number of data: a LinkedList of child pointers (offsets in the file), a LinkedList of TreeObjects, the offset of the parent and its own offset. The BTree writes the node's parent (-1 for root, but it could be anything invalid). 
Then the BTree alternates between writing a child pointer and an object. The first child pointer (an int) is written, then the first object is written (the key (a long) followed by the frequency (an int)), then the second child pointer and the second object are written, etc. until the last child pointer is written (All internal nodes will have one more child than keys). The BTree just writes zeroes in the unused places (although again, it could be any invalid number).
There are two scenarios to consider when writing the nodes to disk. The first scenario is that we are splitting the root. When we split the root we are getting three nodes from one node so we create a new node, move the last t-1 keys to it, move the medium key up to the new root, move children if the old root wasn't a leaf node, then write the new node, the new root and the old root to disk. The second scenario is that we are splitting a node that is not the root. We are only creating one new node. The new node gets the split node's last t-1 keys. The parent of the split node gets its medium key. We move children if necessary then write the nodes back.

### Debugging  

The debugging process prominently occured during the testing of the GeneBankSearch, there were issues with the initialization of Variables within the class. I.E BTree tree would not initialzie due to the file not even though it existed.  

### Journal 

We kept track of our conversations via Slack.
  
Nabil [8:35 PM]  
@Austin Foy Hi Austin. How are you doing? I was wondering if you have an account in GitHub. If you don't have one, create one and let me know your GitHub username and e-mail. I will add you to the repo. (edited)  
  
Austin Foy [8:57 PM]  
I do have a GitHub my Username is AFoy95 at austinfoy@u.boisestate.edu  
  
Nabil [1:17 AM]  
Added. Please check if you can access it.  
  
Austin Foy [6:56 PM]  
I do have access thank you  
  
Maruf [8:11 PM]  
@Austin Foy Hey Austin, I am sorry that we had some communication gap. I actually wrote the parser a couple of days back but I forgot to upload them on GitHub. I saw that you wrote it too. Either way, they pretty much look the same. Would you mind taking a look at the origin master on our GitHub repo? I have done GeneBankCreateTree too. Thanks.  

Austin Foy [9:54 PM]  
@Maruf Hey Maruf I actually tested your code alongside Nabil's BTree code.The code for the GeneBankCreate.java whenever I would run the inorder print method from Nabil's BTree class would receive a blank output.

Austin Foy [10:00 PM]  
I am going to modify the code on my own computer assure proper results  
  
Nabil [2:11 AM]  
@Austin Foy Hey Austin, that sounds awesome! I was a bit confused when I saw similar codes from you two. I wasn't sure whose code I was supposed to use. I used Maruf's at first. But, don't worry. We can also try using yours too. I am sorry that we didn't communicate with you before we started writing the codes. Feel free to let me know if you need any help. I am going to work on the rest tomorrow and try figuring out what the problem is.  
  
Austin Foy [9:55 AM]  
Good news I have tested the BTree search and have found that the reason for a failure that has to do with the way it was inputted from the command line,initially it would never create the btree because the input of in the command line would be a large string and there would be no parsing of the sequence length and degree. Then it would fail to find the file to read from, so i added a parser that looked for the gbk mark to read data. And now it works for the most part.
  
Austin Foy [1:07 PM]  
Hey guys are we ready to submit or have we already submitted  
  
Nabil [1:11 PM]  
Has anyone written the README? If not, I can work on that and get it ready for the submission. When do you guys want to submit? Do you know when the deadline is? 'Cause on the instruction, it says that it's at the end of the semester which should be by this Friday.  
  
Austin Foy [3:24 PM]  
I have written part of the readme for my portion but not the total product, I think we submit once we get the BTreeSearch to work a little better, if we cant do that we submit friday before mdnight  
  
Nabil [1:28 PM]  
Hey Austin, I have just merged your pull requests. For some reason, the program is not working. Can you send me the working codes you have on your computer? Just make a zip file of the whole project, and give me a link to download.

### Final Thoughts

We felt that this project was a good lesson in general about the need to communicate effectively it was hard for us to do so even with a Slack and emails. But having said that we thought this project was fun and challenging.

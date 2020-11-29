package GraphAlgorithms;


public class BinaryHeap {

    private int[] nodes;
    private int pos;

    public BinaryHeap() {
        this.nodes = new int[32];
        for (int i = 0; i < nodes.length; i++) {
            this.nodes[i] = Integer.MAX_VALUE;
        }
        this.pos = 0;
    }

    public void resize() {
        int[] tab = new int[this.nodes.length + 32];
        for (int i = 0; i < nodes.length; i++) {
            tab[i] = Integer.MAX_VALUE;
        }
        System.arraycopy(this.nodes, 0, tab, 0, this.nodes.length);
        this.nodes = tab;
    }

    public boolean isEmpty() {
        return pos == 0;
    }

    public void insert(int element) {
        int positionElement = this.pos;
        this.nodes[positionElement] = element;
        if (positionElement >= this.nodes.length)
            this.resize();

        int positionOfTheParent = this.getPositionOfTheParent(positionElement);
        while(positionOfTheParent >= 0 && this.nodes[positionOfTheParent] > this.nodes[positionElement]) {
            this.swap(positionOfTheParent, positionElement);
            positionElement = positionOfTheParent;
            positionOfTheParent = this.getPositionOfTheParent(positionElement);
        }
        this.pos++;
    }

    private int getPositionOfTheParent(int pos) {
        return (pos - 1) / 2;
    }

    public int remove() {
        this.pos--;
        int lastElementIndex = this.pos;
        this.swap(0, lastElementIndex);
        this.nodes[lastElementIndex] = Integer.MAX_VALUE;

        int bestChild = this.getBestChildPos(0);
        int i = 0;
        while (bestChild != Integer.MAX_VALUE && this.nodes[bestChild] < this.nodes[i]) {
            swap(i, bestChild);
            i = bestChild;
            bestChild = this.getBestChildPos(i);
        }
    	return this.pos;
    }

    private int getBestChildPos(int src) {
        if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
            return Integer.MAX_VALUE;
        } else {
            int left = 2 * src + 1;
            int right = 2 * src + 2;
            if(right >= this.pos)
                return left;

            return this.nodes[left] < this.nodes[right] ? left : right;
        }
    }

    
    /**
	 * Test if the node is a leaf in the binary heap
	 * 
	 * @returns true if it's a leaf or false else
	 * 
	 */	
    private boolean isLeaf(int src) { return 2 * src + 1 >= this.pos; }

    private void swap(int father, int child) {
        int temp = nodes[father];
        nodes[father] = nodes[child];
        nodes[child] = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pos; i++) {
            s.append(nodes[i]).append(", ");
        }
        return s.toString();
    }

    /**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @returns a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    public boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= pos) {
                return nodes[left] >= nodes[root] && testRec(left);
            } else {
                return nodes[left] >= nodes[root] && testRec(left) && nodes[right] >= nodes[root] && testRec(right);
            }
        }
    }

    public static void main(String[] args) {
        BinaryHeap jarjarBin = new BinaryHeap();
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 20;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));
            System.out.print("insert " + rand+"\n");
            jarjarBin.insert(rand);            
            k--;
        }
        System.out.println("\n" + jarjarBin);
        System.out.println(jarjarBin.test());
        System.out.println("\n" + jarjarBin.nodes[0]);
        jarjarBin.remove();
        System.out.println("\n" + jarjarBin);
        System.out.println(jarjarBin.test());
        // ----
        System.out.println("\n" + jarjarBin.nodes[0]);
        jarjarBin.remove();
        System.out.println("\n" + jarjarBin);
        System.out.println(jarjarBin.test());
    }

}

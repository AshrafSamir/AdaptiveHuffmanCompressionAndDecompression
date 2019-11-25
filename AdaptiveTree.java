import java.util.*;


public class AdaptiveTree {

    private Node root;
    private Node current;
    private Node nyt;
    private Node test;

    private static boolean swapFlag = false;
    private static boolean searchFlag = false;
    private static List<Character> inTree = new ArrayList<>();

    boolean flag1 = false;
    boolean flag2 = false;
    AdaptiveTree(){
        this.root = null;
        this.current = null;

    }
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Map<Character,String> shortCode1 = new HashMap<Character,String>();
        Map<String,Character> shortCode2 = new HashMap<String,Character>();
        AdaptiveTree tree = new AdaptiveTree();
        tree.root = new Node(100);
        tree.root.setNodeCode("");
        /*
        shortCode1.put('A',"00");
        shortCode1.put('B',"01");
        shortCode1.put('C',"10");
        shortCode2.put("00",'A');
        shortCode2.put("01",'B');
        shortCode2.put("10",'C');
            */
        int shortCodeLen = 7;



       for(int i = 0 ; i<=127;i++){
           String code = Integer.toBinaryString(i);
           while (code.length() < 7)
               code = '0' + code;
           shortCode1.put((char)i, code);
       }

              for(int i = 0 ; i<=127;i++){
           String code = Integer.toBinaryString(i);
           while (code.length() < 7)
               code = '0' + code;
           shortCode2.put(code, (char)i);
       }



    int n=0;
    do {
        System.out.println("1- Compress    ?");
        System.out.println("2- Decompress  ?");
        System.out.println("3- End program ?");

        n = input.nextInt();
        input.nextLine();

        if (n == 1) {
            System.out.println("Enter your txt to Compress.. ");
            String txt = input.nextLine();
            tree.Compress(shortCode1, txt,tree);
        }
        else if (n == 2){
            System.out.println("Enter your Code to Decompress.. ");
            String txt = input.nextLine();
            tree.Decompress(shortCode2,txt,tree,shortCodeLen);

        }
        else{
            break;
        }

    }while (true);


    }


    public boolean checkOccurrence(char c){
        if(inTree.contains(c)){
            return true;
        }
        else {
            return false;
        }
    }

    public Node insertRightNode(Node parent, Node node){
        parent.setRight(node);
        return  parent;
    }

    public Node insertLeftNode(Node parent, Node node){
        parent.setLeft(node);
        return  parent;
    }

    public boolean swap(Node root,Node current ){

            AdaptiveTree tree = new AdaptiveTree();
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node node = queue.poll();
                if((node.getNodeNumber() > current.getNodeNumber()) && (node.getSymbolCount() <=current.getSymbolCount()) && (tree.checkParent(current,node))){

                    //swap
                    swapFlag = true;
                    System.out.println("root.getNodeNumber()"+node.getNodeNumber()+"current.getNodeNumber()"+current.getNodeNumber());
                    Node rTmp = node.getRight();
                    Node lTmp = node.getLeft();
                    node.setRight(current.getRight());
                    node.setLeft(current.getLeft());
                    current.setRight(rTmp);
                    current.setLeft(lTmp);
                    char charTmp = node.getNodeSymbol();
                    node.setNodeSymbol(current.getNodeSymbol());
                    current.setNodeSymbol(charTmp);
                    int intTmp = node.getSymbolCount();
                    node.setSymbolCount(current.getSymbolCount());
                    current.setSymbolCount(intTmp);
                    //inc count
                    node.incrementCont();
                    generateCode(node);
                    generateCode(current);
                    return swapFlag;


                }
                if (node.getRight() != null) {
                    queue.add(node.getRight());
                }
                if (node.getLeft() != null){
                    queue.add(node.getLeft());
                }

        }
        /*
        if(!swapFlag && root.getRight() != null){
            swap(root.getRight(),current);
        }
        if(!swapFlag && root.getLeft() != null){
            swap(root.getLeft(),current);
        }
        */
        return swapFlag;

    }

    public void generateCode(Node parent){

        if( parent.getRight() != null){

            parent.getRight().setNodeCode(parent.processNodeCodeRight());
            generateCode(parent.getRight());
        }

        if( parent.getLeft() != null){

            parent.getLeft().setNodeCode(parent.processNodeCodeLeft());
            generateCode(parent.getLeft());
        }



    }

    public void generateInc(Node root){

        if((root.getRight() != null) && (root.getLeft() != null) ){
            root.setSymbolCount((root.getLeft().getSymbolCount()+root.getRight().getSymbolCount()));
        }
        if(root.getRight() != null){
            generateInc(root.getRight());
        }
        if(root.getLeft() != null){
            generateInc(root.getLeft());
        }
    }

    private Node goToNode(Node temp,char c){

            if(temp.getNodeSymbol() == c){
                searchFlag = true;
                test = temp;
                return temp;
            }

            if(!searchFlag && temp.getLeft() != null){
                goToNode(temp.getLeft(), c);
            }

            if(!searchFlag && temp.getRight() != null){
                goToNode(temp.getRight(), c);
            }



        return temp;
    }

    boolean checkParent(Node curr, Node parent){
        String p = parent.getNodeCode(), c = curr.getNodeCode();
        int min = 0;
        if(p.length()<c.length())min =p.length();
        else min = c.length();
        for(int i = 0; i < min;i++){
            if(p.charAt(i)!=c.charAt(i)){
                return true;
            }
        }
        return false;
    }

    void Compress(Map shortCode,String txt,AdaptiveTree tree){
        String output = "";

        boolean firstChar = true;
        for (int i = 0 ; i<txt.length() ; i++){

            if (firstChar){

                output+=" "+shortCode.get(txt.charAt(i));


                //Create new node
                Node newNode = new Node(txt.charAt(i),tree.root,1,tree.root.getNodeNumber()-1,"1");
                //create NYT
                int num = tree.root.getNodeNumber()-2;
                String cod = "0";
                Node NYT = new Node(tree.root,0,num,cod);
                //inc parent
                tree.root.incrementCont();
                //put the char inTree to check occurrence

                //insert right then left
                tree.insertRightNode(tree.root, newNode);
                tree.insertLeftNode(tree.root, NYT);
                //set current and NYT
                tree.current = newNode;
                tree.nyt = NYT;


            }
            if(!firstChar && !(tree.checkOccurrence(txt.charAt(i)))){

                //Create new node
                Node newNode = new Node(txt.charAt(i),tree.nyt,1,tree.nyt.processNodeNumberRight(),tree.nyt.processNodeCodeRight());
                //insert right
                tree.insertRightNode(tree.nyt,newNode);
                //create NYT
                int num = tree.nyt.processNodeNumberLeft();
                String cod = tree.nyt.processNodeCodeLeft();
                Node NYT = new Node(tree.nyt,0,num,cod);
                //insert left
                tree.insertLeftNode(tree.nyt,NYT);
                //add char in inTree

                //send NYT and short cod
                output+=" "+tree.nyt.getNodeCode();
                output+=" "+shortCode.get(txt.charAt(i));
                /*
                Node tmp = tree.goToParent(NYT);
                while(tmp.getParent()!=null){

                    if(tree.swap(tree.root, tmp)){
                        swapFlag = false;
                    }
                    else{
                        //inc parent
                        tmp.incrementCont();
                    }

                    tmp = tmp.getParent();
                    if(tmp.getParent() ==null )tmp.incrementCont();
                }
                 */
                boolean donSwap = false, in = false;
                Node curr = NYT.getParent();
                while (curr.getParent()!=null) {
                    if (!donSwap && (tree.swap(tree.root, curr))) {
                        swapFlag = false;
                        donSwap = true;
                        in = true;

                    }
                    if (!in) {
                        curr.incrementCont();
                    }
                    curr = curr.getParent();
                    tree.generateInc(tree.root);
                }
                tree.generateInc(tree.root);

                //set current
                tree.current = newNode;
                tree.nyt = NYT;
            }
            if(!firstChar && (tree.checkOccurrence(txt.charAt(i)))){


                tree.goToNode(tree.root, txt.charAt(i));
                searchFlag = false;
                output+=" "+tree.test.getNodeCode();

                boolean donSwap = false, in = false;
                Node curr = tree.test;
                while (curr.getParent()!=null) {
                    if (!donSwap && (tree.swap(tree.root, curr))) {
                        swapFlag = false;
                        donSwap = true;
                        in = true;

                    }
                    if (!in) {

                        curr.incrementCont();

                    }
                    tree.generateInc(tree.root);
                    curr = curr.getParent();
                }

            }

            firstChar = false;
            inTree.add(txt.charAt(i));
        }
        System.out.println(output);
       // tree.print(tree.root);
    }

    void Decompress(Map<String,Character> shortCode,String txt,AdaptiveTree tree, int len){
        String output = "",code = "";
        Character x ;

        boolean firstChar = true;

        for (int i=0;i<txt.length();){

            if (firstChar){

                for (int j=0;j<txt.length();j++){
                    code+=txt.charAt(j);
                    if(shortCode.get(code)!=null){
                        break;
                    }
                }
                output+=shortCode.get(code);
                firstChar = false;
                x = shortCode.get(code);



                //Create new node
                Node newNode = new Node(x,tree.root,1,tree.root.getNodeNumber()-1,"1");
                //create NYT
                int num = tree.root.getNodeNumber()-2;
                String cod = "0";
                Node NYT = new Node(tree.root,0,num,cod);
                //inc parent
                tree.root.incrementCont();

                //insert right then left
                tree.insertRightNode(tree.root, newNode);
                tree.insertLeftNode(tree.root, NYT);

                //inc i
                i = code.length();
                code = String.valueOf(txt.charAt(i));
            }
            else {

                if(tree.checkNYT(tree.root,code)){

                    code = "";int tmp = i;
                    for (int k = 0;k<len;k++)code+=txt.charAt(++tmp);
                    i+=code.length()+1;


                    flag1 = false;
                    //Create new node
                    Node newNode = new Node('0',nyt,1,tree.nyt.processNodeNumberRight(),tree.nyt.processNodeCodeRight());
                    //insert right
                    tree.insertRightNode(nyt,newNode);
                    //create NYT
                    int num = nyt.processNodeNumberLeft();
                    String cod = nyt.processNodeCodeLeft();
                    Node NYT = new Node(nyt,0,num,cod);
                    //insert left
                    tree.insertLeftNode(nyt,NYT);
                    //set char after splitting

                    newNode.setNodeSymbol(shortCode.get(code));
                    //set output
                    output+=shortCode.get(code);
                    //update
                    boolean donSwap = false, in = false;
                    Node curr = NYT.getParent();
                    while (curr.getParent()!=null) {
                        if (!donSwap && (tree.swap(tree.root, curr))) {
                            swapFlag = false;
                            donSwap = true;
                            in = true;

                        }
                        if (!in) {
                            curr.incrementCont();
                        }
                        curr = curr.getParent();
                        tree.generateInc(tree.root);
                    }
                    tree.generateInc(tree.root);

                    if (i<txt.length()) {
                        code = String.valueOf(txt.charAt(i));
                    }
                }
                if(tree.checkSymbol(tree.root,code)){
                    flag2 = false;

                    code = "";
                    i+=code.length()+1;

                    output+=test.getNodeSymbol();

                    boolean donSwap = false, in = false;
                    Node curr = tree.test;
                    while (curr.getParent()!=null) {
                        if (!donSwap && (tree.swap(tree.root, curr))) {
                            swapFlag = false;
                            donSwap = true;
                            in = true;

                        }
                        if (!in) {
                            curr.incrementCont();
                        }
                        tree.generateInc(tree.root);
                        curr = curr.getParent();
                    }
                    if (i<txt.length()) {
                        code = String.valueOf(txt.charAt(i));
                    }
                }
                if(!tree.checkNYT(tree.root,code) && !tree.checkSymbol(tree.root,code) && (i<txt.length()-1)){
                    i++;
                    code+=String.valueOf(txt.charAt(i));
                }

            }


        }
        System.out.println(output);
        //tree.print(tree.root);


    }


    public boolean checkNYT(Node root,String code){

        if(root.getNodeSymbol() == '0' && root.getSymbolCount() == 0 && root.getNodeCode().equals(code)){
            flag1 = true;
            nyt = root;
            return true;
        }

        if(!flag1 && root.getLeft() != null){
            checkNYT(root.getLeft(),code);
        }

        if(!flag1 && root.getRight() != null){
            checkNYT(root.getRight(),code);
        }

        return flag1;

    }

    public boolean checkSymbol(Node root,String code){

        if(root.getNodeSymbol() != '0' && root.getSymbolCount() != 0 && root.getNodeCode().equals(code)){
            flag2 = true;
            test = root;
            return true;
        }

        if( !flag2 && root.getLeft() != null){
            checkSymbol(root.getLeft(),code);
        }

        if( !flag2 && root.getRight() != null){
            checkSymbol(root.getRight(),code);
        }

        return flag2;

    }

    void print(Node node){

        if (node == null)
            return;

        // first recur on left subtree
        print(node.getLeft());

        // then recur on right subtree
        print(node.getRight());

        // now deal with the node

        System.out.println(node.toString());

       /* if (node.getParent() != null){
            System.out.println(node.getParent().toString());
        } */
    }

}

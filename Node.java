public class Node {

    Node(char symbol, Node parent, int count,int number,String code){
        this.NodeSymbol = symbol;
        this.parent = parent;
        this.right = null;
        this.left = null;
        this.SymbolCount = count;
        this.NodeNumber = number;
        this.NodeCode = code;
    }
    Node(Node parent, int count,int number,String code){
        this.NodeSymbol = '0';
        this.parent = parent;
        this.right = null;
        this.left = null;
        this.SymbolCount = count;
        this.NodeNumber = number;
        this.NodeCode = code;
    }
    Node(int number){
        this.parent = null;
        this.left = null;
        this.right = null;
        this.NodeNumber = number;
        this.SymbolCount = 0;
        this.NodeCode = "";
    }


     private char NodeSymbol;
     private String NodeCode;
     private int NodeNumber;
     private int SymbolCount;
     private Node parent;
     private Node left;
     private Node right;



    public void setNodeCode(String nodeCode) {
        NodeCode = nodeCode;
    }

    void setNodeSymbol(char nodeSymbol) {
        NodeSymbol = nodeSymbol;
    }

    void setSymbolCount(int symbolCount) {
        SymbolCount = symbolCount;
    }

    void setLeft(Node left) {
        this.left = left;
    }

    void setRight(Node right) {
        this.right = right;
    }

    char getNodeSymbol() {
        return NodeSymbol;
    }

    String getNodeCode() {
        return NodeCode;
    }

    int getNodeNumber() {
        return NodeNumber;
    }

    int getSymbolCount() {
        return SymbolCount;
    }

    Node getLeft() {
        return left;
    }

    Node getParent() {
        return parent;
    }

    Node getRight() {
        return right;
    }

    void incrementCont(){
        this.SymbolCount++;
    }

    public int processNodeNumberRight() {
        if(this != null) {
            int tmp = this.getNodeNumber();
            tmp--;
            return tmp;
        }
        else return 0;
    }

    int processNodeNumberLeft() {
        if(this != null){
            int tmp = this.getRight().getNodeNumber();
            tmp--;
            return tmp;
        }
        return 0;
    }

    String processNodeCodeRight() {
        if(this != null)return this.getNodeCode()+"1";
        return "";
    }

    String processNodeCodeLeft() {
        if(this != null) return this.getNodeCode()+"0";
        return "";
    }


    @Override
    public String toString() {

        return "symbol = " + NodeSymbol +"\n" +
                "nodeCounter = " + SymbolCount +"\n" +
                "nodeNumber = " + NodeNumber +"\n"+
                "nodeCode = " + NodeCode +"\n";
    }
}


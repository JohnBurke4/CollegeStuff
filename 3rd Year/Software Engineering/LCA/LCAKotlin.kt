class Node(input: Int) {
    var left: Node? = null
    var right: Node? = null
    val value: Int = input
}

class BT(input: Int) {
    val root: Node? = Node(input)

    fun addValue(value: Int){
        var newNode = Node(value)
        var node: Node? = root
        while (node != null){
            if (value == node.value){
                node = null
            }
            else if(value < node.value){
                if(node.left == null){
                    node.left = newNode
                    node = null
                }
                else{
                    node = node.left
                }
            }
            else {
                if (node.right == null){
                    node.right = newNode
                    node = null
                }
                else {
                    node = node.right
                }
            }
        }
    }

    fun prettyPrintValues(): String{
        return prettyPrint(root, "")
    }

    fun prettyPrint(node : Node?, prefix : String): String{
        if (node == null){
            return prefix + "-null\n"
        }
        else {
            return prefix + "-" + node.value + "\n" + prettyPrint(node.left, prefix + " |") + prettyPrint(node.right, prefix + "  ")
        }
    }

    fun LCA(valueA: Int, valueB: Int): Int{
        var ancestorsA = ancestors(valueA, root, mutableListOf<Int>())
        var ancestorsB = ancestors(valueB, root, mutableListOf<Int>())
        for (ancestorA in ancestorsA){
            for (ancestorB in ancestorsB){
                if (ancestorA == ancestorB){
                    return ancestorA
                }
            }
        }
        return -1
    }

    fun ancestors(value: Int, node: Node?, array: MutableList<Int>): MutableList<Int>{
        var duplicate = array;
        if (node == null){
            return duplicate
        }
        else if (node.value == value){
            duplicate.add(value)
            return duplicate
        }
        else if(duplicate.isEmpty()){
            duplicate = ancestors(value, node.left, duplicate)
            if (duplicate.isEmpty()){
                duplicate = ancestors(value, node.right, duplicate)
            }
            if (!duplicate.isEmpty()){
                duplicate.add(node.value)
            }
            return duplicate
        }
        else {
            return duplicate
        }
    }


}




fun main(){
    
    var bt = BT(10)
    bt.addValue(9)
    bt.addValue(11)
    bt.addValue(8)
    bt.addValue(15)
    bt.addValue(12)
    bt.addValue(1)
    bt.addValue(7)
    bt.addValue(-5)
    bt.addValue(200)
    bt.addValue(5)
    println(bt.prettyPrintValues())
    println(bt.LCA(-5, 12))
}
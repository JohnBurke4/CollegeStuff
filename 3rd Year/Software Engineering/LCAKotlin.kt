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
    println(bt.root)
}
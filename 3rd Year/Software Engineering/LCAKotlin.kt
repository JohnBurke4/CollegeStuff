class Node(input: Int) {
    val left: Node? = null
    val right: Node? = null
    val value: Int = input
}

val root = Node(1)

fun main(){
    println(root.value)
}
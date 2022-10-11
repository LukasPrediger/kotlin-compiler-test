package de.chasenet.compiler


object SyntaxTreePrinter {
    fun print(tree: SyntaxTree) {
        printNode(tree.root, "", true)
    }

    private fun printNode(node: SyntaxNode, indent: String = "", isLast: Boolean = true) {
        val marker = if (isLast) "└──" else "├──"

        print(indent)
        print(marker)
        print(node.kind)

        if (node is SyntaxToken && node.value != null) {
            print(" ")
            print(node.value)
        }

        println()

        val newIndent = indent + if (isLast) "    " else "│   "

        val lastChild = node.children.lastOrNull()

        node.children.forEach {
            printNode(it, newIndent, it == lastChild)
        }
    }
}
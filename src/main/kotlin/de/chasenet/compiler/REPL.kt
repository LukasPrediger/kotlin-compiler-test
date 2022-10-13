package de.chasenet.compiler

fun main() {
    var showTree = false

    while (true) {
        print("> ")

        when (val line = readln()) {
            "#showTree" -> {
                showTree = true
                println("showing tree")
            }

            "#hideTree" -> {
                showTree = false
                println("hiding tree")
            }

            "#exit" -> {
                println("exiting")
                break
            }

            else -> {
                val tree = SyntaxTree.parse(line)

                if (showTree) SyntaxTreePrinter.print(tree)

                tree.diagnostics.forEach {
                    System.err.println(it)
                }

                tree.diagnostics.ifEmpty {
                    println(Evaluator.evaluate(tree))
                }

                println()
            }
        }
    }
}


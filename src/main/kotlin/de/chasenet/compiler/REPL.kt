package de.chasenet.compiler

fun main() {
    while (true) {
        print("> ")
        val line = readln()
        val lexer = Lexer(line)
        println(lexer.parse())

        lexer.diagnostics.forEach {
            System.err.println(it)
        }
    }
}


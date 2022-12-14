package de.chasenet.compiler

import de.chasenet.compiler.TextToken.*

class Lexer(private val line: String) {
    private var position = 0

    private val current: Char?
        get() = line.getOrNull(position)

    private val mutableDiagnostics: MutableList<String> = mutableListOf()

    val diagnostics: List<String>
        get() = mutableDiagnostics

    fun next() {
        position++
    }

    fun nextToken(): SyntaxToken {
        if (position >= line.length) {
            return EofToken(position)
        }

        if (current.isDigit()) {
            return parseNumberToken()
        }
        if (current.isWhitespace()) {
            return parseWhiteSpaceToken()
        }

        return advance {
            when (current) {
                '+' -> OperatorToken.PlusToken(position)
                '-' -> OperatorToken.MinusToken(position)
                '*' -> OperatorToken.StarToken(position)
                '/' -> OperatorToken.SlashToken(position)
                '(' -> OpenParenthesisToken(position)
                ')' -> ClosedParenthesisToken(position)
                else -> {
                    mutableDiagnostics.add("ERROR: bad input character: $current")
                    BadToken(position, line[position].toString())
                }
            }
        }
    }

    private fun <T> advance(block: () -> T): T {
        val value = block()
        next()
        return value
    }

    private fun parseNumberToken(): NumberToken {
        val start = position
        while (current.isDigit()) next()

        val length = position - start
        return NumberToken(start, line.substring(start, start + length))
    }

    private fun parseWhiteSpaceToken(): WhitespaceToken {
        val start = position

        while (current.isWhitespace()) next()

        val length = position - start
        val text = line.substring(start, start + length)
        return WhitespaceToken(start, text)
    }
}
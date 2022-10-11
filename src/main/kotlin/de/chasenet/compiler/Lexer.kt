package de.chasenet.compiler

class Lexer(private val line: String) {
    private var position = 0

    private val current: Char?
        get() = line.getOrNull(position)

    private val mutableDiagnostics: MutableList<String> = mutableListOf()

    val diagnostics: List<String>
        get() = mutableDiagnostics

    fun parse(): List<SyntaxToken<*>> {
        val tokens = mutableListOf<SyntaxToken<*>>()

        var token = nextToken()
        tokens.add(token)
        while (token.kind != SyntaxKind.EOF) {
            token = nextToken()
            tokens.add(token)
        }

        return tokens
    }

    fun next() {
        position++
    }

    private fun nextToken(): SyntaxToken<*> {
        if (position >= line.length) {
            return EofToken(position)
        }

        if (isDigit(current)) {
            return parseNumberToken()
        }
        if (isWhitespace(current)) {
            return parseWhiteSpaceToken()
        }

        return advance {
            when (current) {
                '+' -> OperatorToken.PlusToken(position)
                '-' -> OperatorToken.MinusToken(position)
                '*' -> OperatorToken.StarToken(position)
                '/' -> OperatorToken.SlashToken(position)
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
        while (isDigit(current)) next()

        val length = position - start
        return NumberToken(start, line.substring(start, start + length))
    }

    private fun parseWhiteSpaceToken(): WhitespaceToken {
        val start = position

        while (isWhitespace(current)) next()

        val length = position - start
        val text = line.substring(start, start + length)
        return WhitespaceToken(start, text)
    }
}
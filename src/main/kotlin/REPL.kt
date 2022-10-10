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

abstract class SyntaxToken<T>(
    val start: Int,
    val kind: SyntaxKind,
    val text: String,
) {
    abstract val value: T?

    override fun toString(): String = "$kind(\"$text\" ${value?.toString() ?: ""})"
}

class NumberToken(start: Int, text: String) :
    SyntaxToken<Int>(start, SyntaxKind.NumberToken, text) {
    override val value: Int = text.toInt()
}

class EofToken(start: Int) : SyntaxToken<Unit>(start, SyntaxKind.EOF, "") {
    override val value: Unit = Unit
}

class WhitespaceToken(start: Int, text: String) :
    SyntaxToken<String>(start, SyntaxKind.WhiteSpaceToken, text) {
    override val value: String = text
}

class BadToken(start: Int, text: String) :
    SyntaxToken<String>(start, SyntaxKind.BadToken, text) {
    override val value: String = text
}

sealed class OperatorToken(start: Int, kind: SyntaxKind, text: String) : SyntaxToken<String>(start, kind, text) {
    override val value: String = text

    class PlusToken(start: Int) : OperatorToken(start, SyntaxKind.PlusToken, "+")
    class MinusToken(start: Int) : OperatorToken(start, SyntaxKind.MinusToken, "-")
    class StarToken(start: Int) : OperatorToken(start, SyntaxKind.StarToken, "*")
    class SlashToken(start: Int) : OperatorToken(start, SyntaxKind.SlashToken, "/")
}

enum class SyntaxKind {
    EOF,
    NumberToken,
    WhiteSpaceToken,
    BadToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken
}
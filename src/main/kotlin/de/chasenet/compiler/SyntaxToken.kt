package de.chasenet.compiler

abstract class SyntaxToken<T>(
    val start: Int,
    val kind: SyntaxKind,
    private val text: String,
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
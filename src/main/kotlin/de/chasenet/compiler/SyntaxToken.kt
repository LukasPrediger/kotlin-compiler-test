package de.chasenet.compiler

open class SyntaxToken(
    val position: Int,
    override val kind: SyntaxKind,
    private val text: String,
) : SyntaxNode() {
    open val value: Any? = null

    override val children: List<SyntaxNode> = emptyList()

    override fun toString(): String = "$kind(\"$text\" ${value?.toString() ?: ""})"
}

class NumberToken(start: Int, text: String) :
    SyntaxToken(start, SyntaxKind.NumberToken, text) {
    override val value: Int = text.toInt()
}

class EofToken(start: Int) : SyntaxToken(start, SyntaxKind.EOF, "") {
    override val value: Unit = Unit
}

class WhitespaceToken(start: Int, text: String) :
    SyntaxToken(start, SyntaxKind.WhiteSpaceToken, text) {
    override val value: String = text
}

class BadToken(start: Int, text: String) :
    SyntaxToken(start, SyntaxKind.BadToken, text) {
    override val value: String = text
}

sealed class OperatorToken(start: Int, kind: SyntaxKind, text: String) : SyntaxToken(start, kind, text) {
    override val value: String = text

    class PlusToken(start: Int) : OperatorToken(start, SyntaxKind.PlusToken, "+")
    class MinusToken(start: Int) : OperatorToken(start, SyntaxKind.MinusToken, "-")
    class StarToken(start: Int) : OperatorToken(start, SyntaxKind.StarToken, "*")
    class SlashToken(start: Int) : OperatorToken(start, SyntaxKind.SlashToken, "/")
}
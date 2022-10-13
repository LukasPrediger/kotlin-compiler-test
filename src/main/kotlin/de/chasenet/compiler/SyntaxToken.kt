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

sealed class TextToken(start: Int, kind: SyntaxKind, text: String) : SyntaxToken(start, kind, text) {
    override val value: Any = text

    class WhitespaceToken(start: Int, text: String) : TextToken(start, SyntaxKind.WhiteSpaceToken, text)
    class BadToken(start: Int, text: String) : TextToken(start, SyntaxKind.BadToken, text)
    class EofToken(start: Int) : TextToken(start, SyntaxKind.EOF, "")
    class OpenParenthesisToken(start: Int): TextToken(start, SyntaxKind.OpenParenthesis, "(")
    class ClosedParenthesisToken(start: Int): TextToken(start, SyntaxKind.ClosedParenthesis, ")")
}

sealed class OperatorToken(start: Int, kind: SyntaxKind, text: String) : SyntaxToken(start, kind, text) {
    override val value: String = text

    class PlusToken(start: Int) : OperatorToken(start, SyntaxKind.PlusToken, "+")
    class MinusToken(start: Int) : OperatorToken(start, SyntaxKind.MinusToken, "-")
    class StarToken(start: Int) : OperatorToken(start, SyntaxKind.StarToken, "*")
    class SlashToken(start: Int) : OperatorToken(start, SyntaxKind.SlashToken, "/")
}
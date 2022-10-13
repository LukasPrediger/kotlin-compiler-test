package de.chasenet.compiler

class Parser(private val line: String) {
    private val mutableDiagnostics: MutableList<String> = mutableListOf()

    val diagnostics: List<String>
        get() = mutableDiagnostics


    var position: Int = 0

    val tokens: List<SyntaxToken> = run {
        val lexer = Lexer(line)

        val mutableTokens = mutableListOf<SyntaxToken>()

        lateinit var token: SyntaxToken
        do {
            token = lexer.nextToken()
            if (token.kind != SyntaxKind.BadToken && token.kind != SyntaxKind.WhiteSpaceToken) {
                mutableTokens.add(token)
            }
        } while (token.kind != SyntaxKind.EOF)

        mutableDiagnostics.addAll(lexer.diagnostics)

        return@run mutableTokens
    }

    private val current: SyntaxToken
        get() = peek(0)

    private fun peek(offset: Int): SyntaxToken = tokens.getOrElse(position + offset) { tokens.last() }

    private fun next() {
        position++
    }

    private fun nextToken(): SyntaxToken = current.also { next() }

    private fun match(kind: SyntaxKind): SyntaxToken {
        if (current.kind == kind) {
            return nextToken()
        }
        mutableDiagnostics.add("ERROR: unexpected token <${current.kind}>, expected <$kind>")
        return SyntaxToken(current.position, kind, "")
    }

    fun parse(): SyntaxTree = SyntaxTree(diagnostics, parseExpression(), match(SyntaxKind.EOF))

    private fun parseExpression(): ExpressionSyntax {
        var left: ExpressionSyntax = parsePrimaryExpression()
        while (
            current.kind in listOf(
                SyntaxKind.PlusToken,
                SyntaxKind.MinusToken,
                SyntaxKind.StarToken,
                SyntaxKind.SlashToken
            )
        ) {
            val operatorToken = nextToken()
            val right = parsePrimaryExpression()
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }

        return left
    }

    private fun parsePrimaryExpression(): ExpressionSyntax {
        if (current.kind == SyntaxKind.OpenParenthesis) {
            return ParenthesisedExpression(
                openParenthesisToken = nextToken(),
                expression = parseExpression(),
                closedParenthesisToken = match(SyntaxKind.ClosedParenthesis)
            )
        }

        return NumberExpressionSyntax(match(SyntaxKind.NumberToken))
    }
}

abstract class SyntaxNode {
    abstract val kind: SyntaxKind

    abstract val children: List<SyntaxNode>
}

abstract class ExpressionSyntax : SyntaxNode()

class NumberExpressionSyntax(val numberToken: SyntaxToken) : ExpressionSyntax() {
    override val kind: SyntaxKind = SyntaxKind.NumberExpression

    override val children: List<SyntaxNode> = listOf(numberToken)
}

class ParenthesisedExpression(
    val openParenthesisToken: SyntaxToken,
    val expression: ExpressionSyntax,
    val closedParenthesisToken: SyntaxToken
) : ExpressionSyntax() {
    override val kind: SyntaxKind = SyntaxKind.ParenthesisedExpression

    override val children: List<SyntaxNode> = listOf(openParenthesisToken, expression, closedParenthesisToken)
}

class BinaryExpressionSyntax(
    val left: ExpressionSyntax,
    val operator: SyntaxToken,
    val right: ExpressionSyntax
) : ExpressionSyntax() {
    override val kind: SyntaxKind = SyntaxKind.BinaryExpression

    override val children: List<SyntaxNode> = listOf(left, operator, right)
}

class SyntaxTree(val diagnostics: List<String>, val root: ExpressionSyntax, val endOfFileToken: SyntaxToken) {
    companion object {
        fun parse(text: String): SyntaxTree = Parser(text).parse()
    }
}
package de.chasenet.compiler

enum class SyntaxKind {
    EOF,
    NumberToken,
    WhiteSpaceToken,
    BadToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    NumberExpression,
    BinaryExpression,
    OpenParenthesis,
    ClosedParenthesis,
    ParenthesisedExpression,
}
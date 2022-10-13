package de.chasenet.compiler

object Evaluator {
    fun evaluate(tree: SyntaxTree) = evaluateExpression(tree.root)

    private fun evaluateExpression(node: ExpressionSyntax): Int {
        return when (node) {
            is NumberExpressionSyntax -> node.numberToken.value!! as Int
            is BinaryExpressionSyntax -> {
                val left = evaluateExpression(node.left)
                val right = evaluateExpression(node.right)

                when (node.operator.kind) {
                    SyntaxKind.PlusToken -> left + right
                    SyntaxKind.MinusToken -> left - right
                    SyntaxKind.StarToken -> left * right
                    SyntaxKind.SlashToken -> left / right
                    else -> throw Exception("Unexpected binary operator ${node.operator.kind}")
                }
            }

            else -> throw Exception("Unexpected node ${node.kind}")
        }
    }
}
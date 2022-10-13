package de.chasenet.compiler

fun Char?.isDigit(): Boolean = this?.let(Character::isDigit) ?: false
fun Char?.isWhitespace(): Boolean = this?.let(Character::isWhitespace) ?: false
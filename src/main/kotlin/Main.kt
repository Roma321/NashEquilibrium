package com.example.inrow

import kotlin.math.abs

fun main() {
    val field = Array(3) { Array(3) { -1 } }
    val initState = NashItem(field, null, 1, 0, 1)
    println(initState.bestMove())

}

fun possibleMoves(field: Array<Array<Int>>): MutableList<Move> {
    val moves = mutableListOf<Move>()
    for ((indexRow, row) in field.withIndex())
        for ((indexCol, cell) in row.withIndex()) {
            if (cell == -1)
                moves.add(Move(indexRow, indexCol))
        }
    return moves
}


fun score(player: Int, opponent: Int, field: Array<Array<Int>>): Float {

    if (playerWins(field, player)) return 1f
    if (playerWins(field, opponent)) return -1f
    return 0f
}

class NashItem(
    field: Array<Array<Int>>,
    private var previousMove: Move?,
    private var level: Int,
    private var player: Int,
    private var opponent: Int
) {
    private var children: List<NashItem>
    private var score: Float

    init {
        this.score = score(player, opponent, field)
        children = if (level > Constants.MAX_NASH_DEPTH || abs(this.score) == 1f) {
            emptyList()
        } else {
            val moves = possibleMoves(field)
            moves.map {
                val newField = setMoveInArray(field, it, player)
                NashItem(newField, it, level + 1, opponent, player)
            }
        }
    }

    override fun toString(): String {
        return "NashItem(score=$score, previousMove=$previousMove, level=$level, player=$player, opponent=$opponent, children=${
            children.joinToString(
                "\n" + " ".repeat(level * 4), prefix = if (children.isEmpty()) "" else "\n" + " ".repeat(level * 4)
            )
        })"
    }

    fun bestMove(): Pair<List<Move?>?, Float> {
        if (children.isEmpty()) return Pair(null, this.score)
        val worstForOpponent = children.minBy { it.bestMove().second }
        val opponentsBest = worstForOpponent.bestMove()

        return Pair(
            opponentsBest.first.orEmpty().plus(worstForOpponent.previousMove),
            -opponentsBest.second
        )
    }

}

data class Move(val row: Int, val column: Int)

fun setMoveInArray(
    field: Array<Array<Int>>,
    move: Move,
    player: Int
): Array<Array<Int>> {
    val newField = field.map { it.clone() }.toTypedArray()
    newField[move.row][move.column] = player
    return newField
}

class Constants {
    companion object {
        val MAX_NASH_DEPTH = 9
    }
}

private fun playerWins(field: Array<Array<Int>>, player: Int) =
    (field[0][1] == player && field[0][0] == player && field[0][2] == player)
            || (field[1][1] == player && field[1][0] == player && field[1][2] == player)
            || (field[2][1] == player && field[2][0] == player && field[2][2] == player)
            || (field[0][0] == player && field[1][0] == player && field[2][0] == player)
            || (field[0][1] == player && field[1][1] == player && field[2][2] == player)
            || (field[0][2] == player && field[1][2] == player && field[2][2] == player)
            || (field[0][0] == player && field[1][1] == player && field[2][2] == player)
            || (field[0][2] == player && field[1][1] == player && field[2][0] == player)
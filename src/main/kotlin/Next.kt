enum class Next(val pX: Int, val pY: Int) {
    HORIZONTAL(1, 0),
    VERTICAL(0, 1),
    DIAGONAL_NE_SW(1, 1),
    DIAGONAL_SE_NW(1, -1),
}
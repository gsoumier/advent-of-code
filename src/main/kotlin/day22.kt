import java.util.PriorityQueue

data class Area(val xRange: IntRange, val yRange: IntRange){
    fun intersectWith(other: Area) : Boolean{
        return xRange.intersect(other.xRange).isNotEmpty() && yRange.intersect(other.yRange).isNotEmpty()
    }
}

data class Brick(
    val start: ZCoord,
    val end: ZCoord,
) {
    fun height(): Int = end.z-start.z
    fun area() = Area(start.coord.x..end.coord.x,start.coord.y..end.coord.y)
    fun moveToZ(newZ: Int): Brick {
        return Brick(start.copy(z = newZ), end.copy(z = newZ + height()))
    }
}

class Day22Parser : LineParser<Brick> {
    override fun parseLine(index: Int, line: String): Brick {
        return line.split("~").let { (s, e) -> Brick(
            s.parseZCoord(),
            e.parseZCoord(),
        ) }
    }

    private fun String.parseZCoord() =
        split(",").map { it.toInt() }.let { (x, y, z) -> ZCoord(Coord(x, y), z) }

}

data class ZCoord(val coord: Coord, val z: Int)

class Day22(inputType: InputType = InputType.FINAL) : AocRunner<Brick, Long>(
    22,
    Day22Parser(),
    inputType
) {
    val endUpBricks = fall()

    private fun fall(bricks: List<Brick> = lines): List<Brick> {
        val res = mutableListOf<Brick>()
        val queue = PriorityQueue<Brick>(compareBy { it.start.z })
        queue.addAll(bricks)
        while(queue.isNotEmpty()){
            val next = queue.poll()
            val nextArea = next.area()
            val newZ = (res.filter { it.area().intersectWith(nextArea) }.maxOfOrNull { it.end.z } ?: 0).inc()
            res.add(next.moveToZ(newZ))
        }
        return res
    }

    override fun partOne(): Long {
        return endUpBricks.count {
            findSupportedBricks(it).all { findSupportingBricks(it).size>1 }
        }.toLong()
    }

    override fun partTwo(): Long {
        return endUpBricks.map {
            getFallingBricks(setOf(it))
        }.sumOf { it.size - 1 }.toLong()
    }

    private fun getFallingBricks(fallen: Set<Brick>): Set<Brick> {
        val newFallingBricks = fallen.flatMap { findSupportedBricks(it) }.toSet()
            .filter { it !in fallen }
            .filter { findSupportingBricks(it).all { it in fallen } }
        if(newFallingBricks.isEmpty()){
            return fallen
        }
        return getFallingBricks(fallen + newFallingBricks)
    }

    private fun findSupportedBricks(support: Brick): List<Brick> {
        return endUpBricks.filter { it.start.z == support.end.z + 1 && it.area().intersectWith(support.area()) }
    }

    private fun findSupportingBricks(brick: Brick): List<Brick> {
        return endUpBricks.filter { brick.start.z == it.end.z + 1 && it.area().intersectWith(brick.area()) }
    }

}


fun main() {
    Day22().run()
}

package jacob.pozaic.spaceinvaders.ai

data class Pos(var x: Float, var y: Float) {
    fun moveToward(dest: Pos, direction: Pos, distance: Float): MoveResult {
        // if the step would go further than destination then just go to the destination
        val distance_to_dest = distanceTo(dest).toFloat()
        if(distance_to_dest <= distance)
            return MoveResult(dest, distance - distance_to_dest, true, true)

        val movement_vec = direction.multiScalar(distance)
        println("Start: ($x, $y) Dest: (${dest.x}, ${dest.y}) Distance: $distance_to_dest Direction $direction Movement_VEC $movement_vec")
        return MoveResult(this.add(movement_vec), 0F, true, false)
    }

    fun add(position: Pos) = Pos(this.x + position.x, this.y + position.y)

    fun sub(position: Pos) = Pos(this.x - position.x, this.y - position.y)

    fun multiScalar(distance: Float) = Pos(this.x * distance, this.y * distance)

    fun norm(): Pos {
        val m = Math.sqrt((this.x*this.x).toDouble() + (this.y*this.y).toDouble()).toFloat()
        if(m > 0) return Pos(this.x / m, this.y / m)
        return this
    }

    fun distanceTo(position: Pos) = Math.sqrt(Math.pow(position.x.toDouble() - this.x, 2.0) + Math.pow(position.y.toDouble() - this.y, 2.0))
}
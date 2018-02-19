package jacob.pozaic.spaceinvaders.ai

data class Pos(val x: Float, val y: Float) {
    fun moveToward(dest: Pos, distance: Float): MoveResult {
        // if the step would go further than destination then just go to the destination
        val distance_to_dest = distanceTo(dest).toFloat()
        if(distance_to_dest < distance) return MoveResult(dest, distance - distance_to_dest)
        return MoveResult(this.add(dest.sub(this).norm().multi_scalar(distance)), 0F)
    }

    fun add(position: Pos) = Pos(this.x + position.x, this.y + position.y)

    fun sub(position: Pos) = Pos(this.x - position.x, this.y - position.y)

    fun multi_scalar(distance: Float)= Pos(this.x * distance, this.y * distance)

    fun norm(): Pos {
        val m = mag()
        if(m > 0) return Pos(this.x / m, this.y / m)
        return this
    }

    fun mag() = Math.sqrt((this.x*this.x) + (this.y*this.y).toDouble()).toFloat()

    fun distanceTo(position: Pos) = Math.sqrt(Math.pow(this.x - position.x.toDouble(), 2.0) + Math.pow(this.y - position.y.toDouble(), 2.0))
}
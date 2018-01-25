package jacob.pozaic.spaceinvaders

class Invader(
        size: Float = 0F,
        posX: Float = 0f,
        posY: Float = 0f,
        val type: InvaderType = InvaderType.FIGHTER): Entity(size, size, posX, posY) {
    var current_texture: Int = 0
}
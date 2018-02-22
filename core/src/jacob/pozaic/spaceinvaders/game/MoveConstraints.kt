package jacob.pozaic.spaceinvaders.game

data class Screen(var width: Float,
                  var height: Float,
                  var left: Float,
                  var right: Float,
                  var top: Float,
                  var bottom: Float,
                  var center_x: Float,
                  var center_y: Float,
                  var x_offset: Float,
                  var y_offset: Float)

// The degree to which the screen should be tilted before moving in that direction
const val tilt_sensitivity = 1F

// The speed the player moves at
const val player_speed = 150F

// The pixel location on the x axis where the invaders should drop and reverse direction
var screen: Screen = Screen(800F, 480F, 0F, 0F, 0F, 0F, 400F, 240F, 0F, 0F)

// The pixel location on the y axis where the invaders win if reached
var invader_win_distance = 0F

// Constant offsets, scaled to the screen size
const val spacing_offset = 10F
const val y_offset_lose  = 100F

// Projectile move speed
const val player_projectile_speed = 150F

// TODO: enemy projectile speed differs based on what type it is
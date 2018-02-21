package jacob.pozaic.spaceinvaders.game

// The degree to which the screen should be tilted before moving in that direction
const val tilt_sensitivity = 1F

// The speed the player moves at
const val player_speed = 150F

// The pixel location on the x axis where the invaders should drop and reverse direction
var screen_left_cutoff = 0F
var screen_right_cutoff = 0F
var screen_top_cutoff = 0F

// The pixel location on the y axis where the invaders win if reached
var invader_win_distance = 0F

// Constant offsets, scaled to the screen size
const val x_offset       = 20F
const val spacing_offset = 10F
const val y_offset       = 50F
const val y_offset_lose  = 100F

// Projectile move speed
const val player_projectile_speed = 150F

// TODO: enemy projectile speed differs based on what type it is
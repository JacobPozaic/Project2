package jacob.pozaic.spaceinvaders.entity

// The degree to which the screen should be tilted before moving in that direction
const val tilt_sensitivity = 1F

// The speed the player moves at
const val player_speed = 120F

// The pixel location on the x axis where the invaders should drop and reverse direction
var screen_left_cutoff = 0F
var screen_right_cutoff = 0F
var screen_top_cutoff = 0F

// The pixel location on the y axis where the invaders win if reached
var invader_win_distance = 0F
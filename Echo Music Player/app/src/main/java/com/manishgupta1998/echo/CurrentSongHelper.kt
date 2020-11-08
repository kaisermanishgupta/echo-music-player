package com.manishgupta1998.echo

/**
 * Created by Manish Gupta on 24-01-2018.
 */
class CurrentSongHelper {
    var songArtist : String ?=null
    var songTitle : String ?=null
    var songPath : String ?=null
    var songID : Long = 0
    var currentPosition : Int = 0
    var isPlaying : Boolean = false
    var isLoop : Boolean = false
    var isShuffle : Boolean = false
    var trackPosition : Int = 0
}
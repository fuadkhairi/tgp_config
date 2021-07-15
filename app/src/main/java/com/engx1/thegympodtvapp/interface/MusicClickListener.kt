package com.engx1.thegympodtvapp.`interface`


import com.engx1.thegympodtvapp.model.MusicResponse

interface MusicClickListener {
    fun onClicked(music: MusicResponse, position: Int)
}
package com.example.dkboard.event.dto

data class LikeEvent(
    val postId: Long,
    val createdBy: String,
)

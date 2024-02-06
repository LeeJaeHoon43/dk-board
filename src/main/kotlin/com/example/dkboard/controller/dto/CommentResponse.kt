package com.example.dkboard.controller.dto

data class CommentResponse(
    val id: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

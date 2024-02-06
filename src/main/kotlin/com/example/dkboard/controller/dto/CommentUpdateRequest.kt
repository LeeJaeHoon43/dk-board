package com.example.dkboard.controller.dto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

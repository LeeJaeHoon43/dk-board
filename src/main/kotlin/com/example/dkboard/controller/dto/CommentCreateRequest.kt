package com.example.dkboard.controller.dto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

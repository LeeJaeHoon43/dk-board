package com.example.dkboard.controller.dto

import com.example.dkboard.service.dto.CommentUpdateRequestDto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDto() = CommentUpdateRequestDto(
    content = content,
    updatedBy = updatedBy
)

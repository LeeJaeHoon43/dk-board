package com.example.dkboard.service.dto

import com.example.dkboard.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
    val tags: List<String> = emptyList(),
)

fun PostCreateRequestDto.toEntity() = Post(
    title = title,
    content = content,
    createdBy = createdBy,
    tags = tags
)

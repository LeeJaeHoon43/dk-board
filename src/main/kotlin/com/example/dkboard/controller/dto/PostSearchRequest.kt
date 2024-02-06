package com.example.dkboard.controller.dto

import com.example.dkboard.service.dto.PostSearchRequestDto

data class PostSearchRequest(
    val title: String,
    val createdBy: String,
)

fun PostSearchRequest.toDto() = PostSearchRequestDto(
    title = title,
    createdBy = createdBy
)

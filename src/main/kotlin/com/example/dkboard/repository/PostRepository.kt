package com.example.dkboard.repository

import com.example.dkboard.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}

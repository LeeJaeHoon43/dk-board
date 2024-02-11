package com.example.dkboard.repository

import com.example.dkboard.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByPostId(postId: Long): List<Tag>
}

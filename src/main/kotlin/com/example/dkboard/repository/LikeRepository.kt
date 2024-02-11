package com.example.dkboard.repository

import com.example.dkboard.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long>

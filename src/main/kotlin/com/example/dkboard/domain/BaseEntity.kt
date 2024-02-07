package com.example.dkboard.domain

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
@MappedSuperclass
abstract class BaseEntity(
    createdBy: String,
) {
    val createdBy: String = createdBy
    val createdAt: LocalDateTime = LocalDateTime.now()
    var updatedBy: String? = null
        protected set
    var updatedAt: LocalDateTime? = null
        protected set

    fun updatedBy(updateBy: String) {
        this.updatedBy = updateBy
        this.updatedAt = LocalDateTime.now()
    }
}

package com.example.dkboard.domain

import jakarta.persistence.*

@Entity
@Table(name = "likes")
class Like(
    post: Post,
    createdBy: String,
) : BaseEntity(createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post = post
        protected set
}

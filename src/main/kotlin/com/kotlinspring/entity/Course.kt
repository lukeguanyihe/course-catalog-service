package com.kotlinspring.entity

import javax.persistence.*
import javax.persistence.FetchType.*


@Entity
@Table(name = "Courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var name: String,
    var category: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="INSTRUCTOR_ID", nullable = false)
    val instructor: Instructor? = null
)
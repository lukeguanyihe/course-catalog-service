package com.kotlinspring.entity

import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "Courses")
data class Course(
    val id: Int?,
    val name: String,
    val category: String
)
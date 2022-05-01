package com.kotlinspring.entity

import javax.persistence.*

@Entity
@Table(name = "INSTRUCTORS")
data class Instructor (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var name: String
)
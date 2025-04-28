package com.example.popshelf.domain

data class Shelf(
    val id: Int,
    val image: String?,
    val name: String,
    val color: String,
    val itemCount: Int
)
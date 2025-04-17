package com.example.popshelf.presentation

enum class MediaType(val id: Int) {
    Books(0),
    Games(1),
    Movies(2);

    companion object {
        fun valueOf(id: Int): MediaType {
            return entries.find { it.id == id } ?: return Books
        }
    }
}
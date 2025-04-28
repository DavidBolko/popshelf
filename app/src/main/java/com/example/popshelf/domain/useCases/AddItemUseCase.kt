package com.example.popshelf.domain.useCases

import com.example.popshelf.domain.repository.ShelfItemRepositary
import com.example.popshelf.domain.repository.ShelfRepositary


class AddItemUseCase(private val shelfItemRepositary: ShelfItemRepositary) {
    suspend fun execute(id: String, mediaType: String, status: String, rating: Int, comment:String, shelf:String) {
        if(comment.isNotEmpty()){
            return shelfItemRepositary.addShelfItem(id, mediaType, status, rating, comment, shelf)
        }
        return shelfItemRepositary.addShelfItem(id, mediaType, status, rating, shelf=shelf)
    }
}

package com.example.popshelf.domain.repository

import com.example.popshelf.data.local.entity.ShelfItemEntity
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.MediaType

/** Repository interface for accessing content of individual shelves.
 *  @author David Bolko
 */
interface ShelfItemRepositary {
    suspend fun addShelfItem(id: String, mediaType: String, status: String, rating: Int, comment:String = "", shelf:String)
    suspend fun getAllBooks(): List<MediaItem>
    suspend fun getShelfItems(id:Int?): List<MediaItem>
    suspend fun deleteShelfItem(itemId: String)
    suspend fun updateShelfItem(itemId: String, shelfId: Int, status: String, rating: Int, comment: String)
}
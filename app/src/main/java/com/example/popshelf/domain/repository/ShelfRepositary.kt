package com.example.popshelf.domain.repository

import com.example.popshelf.domain.Shelf

/** Repository interface for accessing data of individual shelves.
 *  @author David Bolko
 */
interface ShelfRepositary {
    suspend fun createShelf(name: String, color:String)
    suspend fun getAllShelves(isSystem:Boolean): List<Shelf>
    suspend fun createDefaultShelves()
}
package com.example.popshelf.domain.repository

import com.example.popshelf.domain.Shelf

/** Repository interface for accessing data of individual shelves.
 */
interface IShelfRepository {

    /**
     * Creates a new user shelf.
     *
     * @param name name of the shelf.
     * @param color color representation for the shelf.
     */
    suspend fun createShelf(name: String, color:String)

    /**
     * Retrieves all shelves.
     *
     * @param isSystem If `true`, returns both system and user shelves, if false it returns only user shelves.
     * @return List of [Shelf]s.
     */
    suspend fun getAllShelves(isSystem:Boolean): List<Shelf>

    /**
     * Creates all system shelves
     */
    suspend fun createDefaultShelves()

    /**
     * deletes a shelf by it's id
     */
    suspend fun deleteShelf(id:String)
}
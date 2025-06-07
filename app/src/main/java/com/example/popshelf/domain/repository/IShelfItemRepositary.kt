package com.example.popshelf.domain.repository

import com.example.popshelf.domain.MediaItem

/** Repository interface for accessing content of individual shelves.
 */
interface IShelfItemRepositary {
    /**
     * Adds a new media item to a shelf.
     *
     * If the shelf parameter is set to "None", the item will be added only to the system shelf.
     *
     * @param id identifier of the media item.
     * @param mediaType type of media.
     * @param status current status
     * @param rating user rating for the item.
     * @param comment optional user comment.
     * @param shelf name of the user shelf or "None".
     */
    suspend fun addShelfItem(id: String, mediaType: String, status: String, rating: Int, comment:String = "", shelf:String)

    /**
     * Retrieves all books from the users collection.
     *
     * @return list of [MediaItem]s.
     */
    suspend fun getAllBooks(): List<MediaItem>

    /**
     * Retrieves all items from a specific shelf.
     *
     * @param id id of the shelf (nullable for default system shelves).
     * @return list of [MediaItem]s.
     */
    suspend fun getShelfItems(id:Int?): List<MediaItem>

    /**
     * Deletes a media item from all shelves.
     *
     * @param itemId ID of the item to remove.
     */
    suspend fun deleteShelfItem(itemId: String)

    /**
     * Updates an existing shelf item with new data.
     *
     * @param itemId identifier of the item to update.
     * @param shelfId shelf ID (nullable for removing from user shelf).
     * @param status  status string.
     * @param rating  user rating.
     * @param comment user comment.
     */
    suspend fun updateShelfItem(itemId: String, shelfId: Int?, status: String, rating: Int, comment: String)

    /**
     * Deletes items from shelves by their ids
     * @param ids list of IDs
     */
    suspend fun deleteItemsFromShelves(ids: List<String>)
}
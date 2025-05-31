package com.example.popshelf.presentation


/*** Enumeration class which represents media type.
 * @author David Bolko
 * @property title - UI representaion of individual media states.
 * @property number - Integer representaion of individual media states.
 */
enum class MediaType(val title: String, val number: Int) {
    BOOKS("Books", 1),
    MOVIES("Movies", 2),
    GAMES("Games", 3),
}
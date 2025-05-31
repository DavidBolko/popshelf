package com.example.popshelf.presentation

/***
 * Enumeration class which represents user status for each media work.
 * @author David Bolko
 * @property title - UI representaion of individual media states.
 */
enum class MediaStatus(val title: String) {
    FINISHED("Finished"),
    PLANNED("Planned"),
    ONGOING("Ongoing")
}
package com.example.popshelf.domain

/**
 * Interface for checking the current network connectivity status.
 */
interface NetworkStatusProvider {
    fun isOnline(): Boolean
}
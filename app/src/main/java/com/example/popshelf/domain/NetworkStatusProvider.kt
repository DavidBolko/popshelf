package com.example.popshelf.domain

interface NetworkStatusProvider {
    fun isOnline(): Boolean
}
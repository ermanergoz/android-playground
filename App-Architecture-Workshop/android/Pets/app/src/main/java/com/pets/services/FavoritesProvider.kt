package com.pets.services

import com.pets.models.Pet
import com.pets.services.Interfaces.FavoriteService

class FavoritesProvider: FavoriteService {
    private val ids = mutableSetOf<Int>()

    override fun fetchFavorites(): MutableSet<Int> {
        return ids
    }

    override suspend fun fetchList(): List<Pet> {
        val ids = fetchFavorites()
        val petList = ArrayList<Pet>()
        val service = ServiceBuilder.buildService(FavoriteService::class.java)
        val list = service.fetchList()
        for (id in ids) {
            list.first { it.id == id }.let {
                petList.add(it)
            }
        }
        return petList
    }

    override fun addToFavorites(id: Int) {
        val ids = fetchFavorites()
        ids.add(id)
    }

    override fun removeFromFavorites(id: Int) {
        val ids = fetchFavorites()
        ids.remove(id)
    }
}
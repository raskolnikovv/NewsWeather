package com.ehve.newsweather.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ehve.newsweather.domain.model.NewsArticle
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "favorites")

class FavManager(private val context: Context) {
    private val gson = Gson()
    private val FAV_KEY = stringSetPreferencesKey("fav_articles")

    // Salva a lista de favoritos
    suspend fun saveFavorites(articles: List<NewsArticle>) {
        val jsonSet = articles.map { gson.toJson(it) }.toSet()
        context.dataStore.edit { prefs ->
            prefs[FAV_KEY] = jsonSet
        }
    }

    // Lê os favoritos e transforma de JSON para Objeto
    val favoritesFlow: Flow<List<NewsArticle>> = context.dataStore.data.map { prefs ->
        val jsonSet = prefs[FAV_KEY] ?: emptySet()
        jsonSet.map { json ->
            gson.fromJson(json, NewsArticle::class.java)
        }
    }
}
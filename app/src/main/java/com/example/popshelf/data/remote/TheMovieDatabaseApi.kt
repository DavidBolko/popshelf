package com.example.popshelf.data.remote

import com.Secrets
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val httpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", Secrets.imdb)
            .header("Accept", "application/json")
            .build()
        chain.proceed(request)
    }
    .build()
private val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").client(httpClient).addConverterFactory(GsonConverterFactory.create()).build()
val movieApi: TmdbApiService = retrofit.create(TmdbApiService::class.java)

/**
 * Retrofit interface for accessing movies and tv-shows data from TMDB API.
 */
interface TmdbApiService {

    /**
     * Fetches and retrieves detailed information for a specific movie by its ID.
     *
     * @param movieId The TMDB movie ID.
     * @return A [tmdbDetailResponse] containing details about movie.
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): tmdbDetailResponse

    /**
     * Fetches and retrieves detailed information for a specific tv-show by its ID.
     *
     * @param tvId The TMDB TV show ID.
     * @return A [tmdbDetailResponse] containing full TV show metadata.
     */
    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(@Path("tv_id") tvId: String): tmdbDetailResponse


    /**
     * Searches and retrieves movies that matches query string criteria.
     *
     * @param query The search term.
     * @param page The result page number (pagination).
     * @return A [MovieSearchResponse] containing the results.
     */
    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int = 1): MovieSearchResponse

    /**
     * Searches and retrieves tv-shows that matches query string criteria.
     *
     * @param query The search term.a
     * @param page The result page number (pagination).
     * @return A [MovieSearchResponse] containing the results.
     */
    @GET("search/tv")
    suspend fun searchTvShows(@Query("query") query: String,@Query("page") page: Int): TvShowSearchResponse
}


//Typy, ktore sa vracajú z endpointov.

/**
 * Represents a simplified movie object returned in search results, every attribute is hardly defined by TMDB API service.
 */
data class Movie(
    val id: Int,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val vote_average: Double?
)

/**
 * Represents a simplified TV show object returned in search results, every attribute is hardly defined by TMDB API service.
 */
data class TvShow(
    val id: Int,
    val name: String?,
    val overview: String?,
    val poster_path: String?,
    val first_air_date: String?,
    val vote_average: Double?
)

/**
 * Detail response for a movie or TV show.
 */
data class tmdbDetailResponse(
    val id: Int,
    val title: String,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val genres: List<Genre>,
    val production_companies: List<ProductionCompany>,
    val tagline: String?
)

/**
 * Represents a genre in the TMDB API.
 */
data class Genre(
    val id: Int,
    val name: String
)

/**
 * Represents a production company involved in a movie or TV show.
 */
data class ProductionCompany(
    val id: Int,
    val name: String,
    val logo_path: String?,
    val origin_country: String
)

/**
 * Wrapper around movies search results and pagination.
 */
data class MovieSearchResponse(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
)
/**
 * Wrapper around TV-Show search results and pagination.
 */
data class TvShowSearchResponse(
    val page: Int,
    val results: List<TvShow>,
    val total_results: Int,
    val total_pages: Int
)
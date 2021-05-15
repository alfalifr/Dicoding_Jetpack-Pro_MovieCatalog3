package sidev.app.course.dicoding.moviecatalog1.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowListResponse
import sidev.app.course.dicoding.moviecatalog1.util.Const

interface ShowApi {
    @GET(Const.PATH_MOVIE_POPULAR)
    fun getPopularMovieList(
        @Query(Const.KEY_PAGE) page: Int = 1,
        @Query(Const.KEY_API_KEY) apiKey: String = Const.API_KEY,
    ): Call<ShowListResponse>

    @GET(Const.PATH_TV_POPULAR)
    fun getPopularTvList(
        @Query(Const.KEY_PAGE) page: Int = 1,
        @Query(Const.KEY_API_KEY) apiKey: String = Const.API_KEY,
    ): Call<ShowListResponse>

    @GET("movie/{id}")
    fun getMovieDetail(
        @Path("id") id: String,
        @Query(Const.KEY_API_KEY) apiKey: String = Const.API_KEY,
    ): Call<ShowDetail>

    @GET("tv/{id}")
    fun getTvDetail(
        @Path("id") id: String,
        @Query(Const.KEY_API_KEY) apiKey: String = Const.API_KEY,
    ): Call<ShowDetail>
}
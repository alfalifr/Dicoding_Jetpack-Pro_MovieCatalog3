package sidev.app.course.dicoding.moviecatalog1.data.datasource

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.Util
import sidev.app.course.dicoding.moviecatalog1.util.Util.getDouble
import sidev.app.course.dicoding.moviecatalog1.util.Util.getIntOrNull
import sidev.app.course.dicoding.moviecatalog1.util.Util.getString
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import sidev.app.course.dicoding.moviecatalog1.data.Result
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.Failure

/**
 * Non Retrofit remote data source for [Show] and [ShowDetail].
 */
object ShowRemoteSource: ShowDataSource {
    override suspend fun getPopularMovieList(c: Context?): Result<List<Show>> = getPopularShowList(c, Const.ShowType.MOVIE)
    override suspend fun getPopularTvList(c: Context?): Result<List<Show>> = getPopularShowList(c, Const.ShowType.TV)

    override suspend fun getMovieDetail(c: Context?, id: String): Result<ShowDetail> = getShowDetail(c, Const.ShowType.MOVIE, id)
    override suspend fun getTvDetail(c: Context?, id: String): Result<ShowDetail> = getShowDetail(c, Const.ShowType.TV, id)

    private suspend fun getPopularShowList(c: Context?, type: Const.ShowType): Result<List<Show>> = suspendCoroutine {
        Util.httpGet(
            c,
            type.getPopularUrl(),
            doOnNotSucces(it)
        ){ _, content ->
            val shows = content.parseShowList()
            it.resume(Success(shows))
        }
    }

    private suspend fun getShowDetail(c: Context?, type: Const.ShowType, showId: String): Result<ShowDetail> = suspendCoroutine { cont ->
        Util.httpGet(
            c,
            type.getDetailUrl(showId),
            doOnNotSucces(cont)
        ) { _, content ->
            val json = JsonParser.parseString(content).asJsonObject
            val genreArray = json.getAsJsonArray(Const.KEY_GENRES)
            val genres = ArrayList<String>(genreArray.size())
            genreArray.forEach {
                genres += it.asJsonObject.getString(Const.KEY_NAME)
            }
            val show = json.parseShow()
            val showDetail = ShowDetail(
                show, genres,
                json.getIntOrNull(Const.KEY_MOVIE_DURATION),
                json.getString(Const.KEY_TAGLINE),
                json.getString(Const.KEY_OVERVIEW),
                json.getString(Const.KEY_BACKDROP),
            )
            cont.resume(Success(showDetail))
        }
    }

    private fun <T> doOnNotSucces(continuation: Continuation<Result<T>>) = { code: Int, e: Exception? ->
        continuation.resume(Failure(code, e))
    }

    private fun String.parseShowList(): List<Show> {
        val json = JsonParser.parseString(this).asJsonObject
        val jsonArray = json.getAsJsonArray(Const.KEY_RESULTS)
        val movies = ArrayList<Show>(jsonArray.size())
        for(i in 0 until jsonArray.size()){
            val movieJson = jsonArray[i].asJsonObject
            movies += movieJson.parseShow()
        }
        return movies
    }
    private fun JsonObject.parseShow(): Show = Show(
        getString(Const.KEY_ID),
        (if(has(Const.KEY_TITLE)) getString(Const.KEY_TITLE)
        else getString(Const.KEY_NAME)),
        getString(Const.KEY_IMG),
        (if(has(Const.KEY_RELEASE)) getString(Const.KEY_RELEASE)
        else getString(Const.KEY_FIRST_AIR_DATE)),
        getDouble(Const.KEY_RATING),
    )
}
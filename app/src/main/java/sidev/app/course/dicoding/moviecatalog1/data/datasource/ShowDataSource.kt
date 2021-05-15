package sidev.app.course.dicoding.moviecatalog1.data.datasource

import android.content.Context
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.Result
import java.io.Serializable

interface ShowDataSource: Serializable {
    suspend fun getPopularMovieList(c: Context?): Result<List<Show>>
    suspend fun getPopularTvList(c: Context?): Result<List<Show>>
    suspend fun getMovieDetail(c: Context?, id: String): Result<ShowDetail>
    suspend fun getTvDetail(c: Context?, id: String): Result<ShowDetail>
}
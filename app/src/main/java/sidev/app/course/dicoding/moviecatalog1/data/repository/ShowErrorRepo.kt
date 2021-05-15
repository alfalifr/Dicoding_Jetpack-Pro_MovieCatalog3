package sidev.app.course.dicoding.moviecatalog1.data.repository

import android.content.Context
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import java.io.IOException
import sidev.app.course.dicoding.moviecatalog1.data.Result
import sidev.app.course.dicoding.moviecatalog1.data.Failure

object ShowErrorRepo: ShowRepo {
    override suspend fun getPopularMovieList(c: Context?): Result<List<Show>> = Failure(404, IOException("Not found"))
    override suspend fun getPopularTvList(c: Context?): Result<List<Show>> = Failure(-1, IOException("Unknown"))
    override suspend fun getMovieDetail(c: Context?, id: String): Result<ShowDetail> = Failure(-1, IOException("Still unknown"))
    override suspend fun getTvDetail(c: Context?, id: String): Result<ShowDetail> = Failure(10, IOException("I don't even know what is that code"))
}
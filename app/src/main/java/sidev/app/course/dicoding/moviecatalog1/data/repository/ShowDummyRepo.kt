package sidev.app.course.dicoding.moviecatalog1.data.repository

import android.content.Context
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.data.Result
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.util.Dummy

object ShowDummyRepo: ShowRepo {
    override suspend fun getPopularMovieList(c: Context?): Result<List<Show>> = Success(listOf(Dummy.dummyMovieItem))
    override suspend fun getPopularTvList(c: Context?): Result<List<Show>> = Success(listOf(Dummy.dummyTvItem))
    override suspend fun getMovieDetail(c: Context?, id: String): Result<ShowDetail> = Success(Dummy.dummyMovieDetail)
    override suspend fun getTvDetail(c: Context?, id: String): Result<ShowDetail> = Success(Dummy.dummyTvDetail)
}
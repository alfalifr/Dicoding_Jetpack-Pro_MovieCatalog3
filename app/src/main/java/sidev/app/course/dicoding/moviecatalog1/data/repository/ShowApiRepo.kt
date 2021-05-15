package sidev.app.course.dicoding.moviecatalog1.data.repository

import android.content.Context
import sidev.app.course.dicoding.moviecatalog1.data.datasource.ShowDataSource
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.Result

class ShowApiRepo(private val remoteDataSource: ShowDataSource): ShowRepo {
    override suspend fun getPopularMovieList(c: Context?): Result<List<Show>> = remoteDataSource.getPopularMovieList(c)
    override suspend fun getPopularTvList(c: Context?): Result<List<Show>> = remoteDataSource.getPopularTvList(c)

    override suspend fun getMovieDetail(c: Context?, id: String): Result<ShowDetail> = remoteDataSource.getMovieDetail(c, id)
    override suspend fun getTvDetail(c: Context?, id: String): Result<ShowDetail> = remoteDataSource.getTvDetail(c, id)
}
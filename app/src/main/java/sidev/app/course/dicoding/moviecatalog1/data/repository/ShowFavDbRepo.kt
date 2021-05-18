package sidev.app.course.dicoding.moviecatalog1.data.repository

import android.content.Context
import androidx.paging.PagingSource
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDao
import sidev.app.course.dicoding.moviecatalog1.data.model.Show


class ShowFavDbRepo(private val dao: ShowFavDao): ShowFavRepo {
    override suspend fun getPopularMovieList(c: Context?): PagingSource<Int, Show> = dao.getFavMovies()
    override suspend fun getPopularTvList(c: Context?): PagingSource<Int, Show> = dao.getFavTv()
}
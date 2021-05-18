package sidev.app.course.dicoding.moviecatalog1.data.repository

import android.content.Context
import androidx.paging.PagingSource
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDao
import sidev.app.course.dicoding.moviecatalog1.data.model.Show

class ShowFavRepo(private val dao: ShowFavDao) {
    fun getPopularMovieList(): PagingSource<Int, Show> = dao.getFavMovies()
    fun getPopularTvList(): PagingSource<Int, Show> = dao.getFavTv()
}
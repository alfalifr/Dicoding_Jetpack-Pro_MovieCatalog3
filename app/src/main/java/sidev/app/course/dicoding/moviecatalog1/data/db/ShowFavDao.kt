package sidev.app.course.dicoding.moviecatalog1.data.db

import androidx.paging.PagingSource
import androidx.room.*
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.util.Const

@Dao
interface ShowFavDao {
    @Query("DELETE FROM show_fav")
    fun clear()

    @Query("SELECT * FROM show_fav WHERE type = :type")
    fun getShows(type: Int): PagingSource<Int, Show>
    @Query("SELECT * FROM show_fav WHERE type = :type AND id = :id")
    fun getShowById(type: Int, id: String): Show?

    fun getFavMovies(): PagingSource<Int, Show> = getShows(Const.ShowType.MOVIE.ordinal)
    fun getFavTv(): PagingSource<Int, Show> = getShows(Const.ShowType.TV.ordinal)

    fun getFavMovieById(id: String): Show? = getShowById(Const.ShowType.MOVIE.ordinal, id)
    fun getFavTvById(id: String): Show? = getShowById(Const.ShowType.TV.ordinal, id)

    fun isFav(type: Int, id: String): Boolean = getShowById(type, id) != null
    fun isFav(show: Show): Boolean = isFav(show.type, show.id)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(show: Show)

    @Delete
    fun delete(show: Show): Int

    fun deleteMovieById(id: String): Int = getFavMovieById(id)?.let { delete(it) } ?: 0
    fun deleteTvById(id: String): Int = getFavTvById(id)?.let { delete(it) } ?: 0
}
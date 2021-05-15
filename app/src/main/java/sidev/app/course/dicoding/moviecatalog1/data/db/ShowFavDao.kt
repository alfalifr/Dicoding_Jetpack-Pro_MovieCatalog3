package sidev.app.course.dicoding.moviecatalog1.data.db

import androidx.paging.DataSource
import androidx.room.*
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.util.Const

@Dao
interface ShowFavDao {
    @Query("SELECT * FROM show_fav WHERE type = :type")
    fun getShows(type: Int): DataSource.Factory<Int, Show>
    @Query("SELECT * FROM show_fav WHERE type = :type AND id = :id")
    fun getShowById(type: Int, id: String): Show?

    fun getFavMovies(): DataSource.Factory<Int, Show> = getShows(Const.ShowType.MOVIE.ordinal)
    fun getFavTv(): DataSource.Factory<Int, Show> = getShows(Const.ShowType.TV.ordinal)

    fun getFavMovieById(id: String): Show? = getShowById(Const.ShowType.MOVIE.ordinal, id)
    fun getFavTvById(id: String): Show? = getShowById(Const.ShowType.TV.ordinal, id)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(show: Show)

    @Delete
    fun delete(show: Show): Int

    fun deleteMovieById(id: String): Int = getFavMovieById(id)?.let { delete(it) } ?: 0
    fun deleteTvById(id: String): Int = getFavTvById(id)?.let { delete(it) } ?: 0
}
package sidev.app.course.dicoding.moviecatalog1.repo

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDao
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDb
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowFavRepo
import sidev.app.course.dicoding.moviecatalog1.util.Dummy

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ShowFavRepoTest {

    companion object {
        @JvmStatic
        private lateinit var db: ShowFavDb
        private lateinit var dao: ShowFavDao
        private lateinit var repo: ShowFavRepo

        private val dummyTv = Dummy.dummyTvItem
        private val dummyMovie = Dummy.dummyMovieItem
        private val dummyTv2 = Dummy.dummyTvItem2
        private val dummyMovie2 = Dummy.dummyMovieItem2

        @BeforeClass
        @JvmStatic
        fun setUp(){
            //val inst = InstrumentationRegistry.getInstrumentation()
            val ctx = ApplicationProvider.getApplicationContext<Context>() //ApplicationProvider.getApplicationContext<Context>() //inst.context //Robolectric.buildActivity(SplashActivity::class.java).create().get() //
            db = Room.inMemoryDatabaseBuilder(ctx, ShowFavDb::class.java).build()
            dao = db.dao()
            repo = ShowFavRepo(dao)
        }

        @AfterClass
        @JvmStatic
        fun clear(){
            db.close()
        }
    }

    @Test
    fun _1_insertFav(){
        repo.insertFav(dummyMovie)
        repo.insertFav(dummyTv)

        val movieFromDb = repo.getFavMovie(dummyMovie.id)
        val tvFromDb = repo.getFavTv(dummyTv.id)

        assertEquals(dummyMovie, movieFromDb)
        assertEquals(dummyTv, tvFromDb)
    }

    @Test
    fun _2_isShowFav(){
        val isFavMovie1 = repo.isShowFav(dummyMovie.type, dummyMovie.id)
        val isFavTv1 = repo.isShowFav(dummyTv.type, dummyTv.id)

        val isFavMovie2 = repo.isShowFav(dummyMovie2.type, dummyMovie2.id)
        val isFavTv2 = repo.isShowFav(dummyTv2.type, dummyTv2.id)

        assertEquals(true, isFavMovie1)
        assertEquals(true, isFavTv1)
        assertEquals(false, isFavMovie2)
        assertEquals(false, isFavTv2)
    }

    @Test
    fun _3_getPopularMovieList(){
        runBlocking {
            val p = repo.getPopularMovieList().load(PagingSource.LoadParams.Append(0, 1, true))
            assert(p is PagingSource.LoadResult.Page)
            val list = (p as PagingSource.LoadResult.Page).data
            assert(list.isNotEmpty())
            assertEquals(listOf(dummyMovie), list)
        }
    }

    @Test
    fun _4_getPopularTvList(){
        runBlocking {
            val p = repo.getPopularTvList().load(PagingSource.LoadParams.Append(0, 1, true))
            assert(p is PagingSource.LoadResult.Page)
            val list = (p as PagingSource.LoadResult.Page).data
            assert(list.isNotEmpty())
            assertEquals(listOf(dummyTv), list)
        }
    }

    @Test
    fun _5_deleteFav(){
        val deletedMovieCount = repo.deleteFav(dummyMovie)
        val deletedTvCount = repo.deleteFav(dummyTv)

        val deletedMovieCount2 = repo.deleteFav(dummyMovie2)
        val deletedTvCount2 = repo.deleteFav(dummyTv2)

        assertEquals(1, deletedMovieCount)
        assertEquals(1, deletedTvCount)
        assertEquals(0, deletedMovieCount2)
        assertEquals(0, deletedTvCount2)

        val isFavTv = repo.isShowFav(dummyTv.type, dummyTv.id)
        val isFavMovie = repo.isShowFav(dummyMovie.type, dummyMovie.id)

        assertEquals(false, isFavTv)
        assertEquals(false, isFavMovie)
    }
}
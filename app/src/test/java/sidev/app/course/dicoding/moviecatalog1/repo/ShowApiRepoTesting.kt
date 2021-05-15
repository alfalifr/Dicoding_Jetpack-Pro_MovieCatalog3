package sidev.app.course.dicoding.moviecatalog1.repo

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito.*
import sidev.app.course.dicoding.moviecatalog1.data.datasource.ShowDataSource
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowApiRepo
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig

class ShowApiRepoTesting {

    companion object {
        // These props are lazy val so there's no way the value will be changed after initialization.
        private val remoteSrc: ShowDataSource by lazy { mock(ShowDataSource::class.java) }
        private val repo: ShowApiRepo by lazy { ShowApiRepo(remoteSrc) }

        private val movie = AppConfig.dummyMovieItem
        private val movieDetail = AppConfig.dummyMovieDetail
        private val tv = AppConfig.dummyTvItem
        private val tvDetail = AppConfig.dummyTvDetail

        @BeforeClass
        @JvmStatic
        fun initSetup(): Unit = runBlocking {
            `when`(remoteSrc.getPopularMovieList(any())).thenReturn(
                Success(listOf(movie))
            )
            `when`(remoteSrc.getMovieDetail(any(), anyString())).thenReturn(
                Success(movieDetail)
            )

            `when`(remoteSrc.getPopularTvList(any())).thenReturn(
                Success(listOf(tv))
            )
            `when`(remoteSrc.getTvDetail(any(), anyString())).thenReturn(
                Success(tvDetail)
            )
        }
    }

    @Test
    fun getPopularMovieList(): Unit = runBlocking {
        val list = repo.getPopularMovieList(null)
        verify(remoteSrc).getPopularMovieList(null)
        assert(list is Success)
        val dataFromList = (list as Success).data.first()
        assertEquals(movie, dataFromList)
    }

    @Test
    fun getPopularTvList(): Unit = runBlocking {
        val list = repo.getPopularTvList(null)
        verify(remoteSrc).getPopularTvList(null)
        assert(list is Success)
        val dataFromList = (list as Success).data.first()
        assertEquals(tv, dataFromList)
    }

    @Test
    fun getMovieDetail(): Unit = runBlocking {
        val data = AppConfig.dummyMovieDetail

        val detail = repo.getMovieDetail(null, data.show.id)
        verify(remoteSrc).getMovieDetail(null, data.show.id)
        assert(detail is Success)
        val dataFromCall = (detail as Success).data
        assertEquals(movieDetail, dataFromCall)
    }

    @Test
    fun getTvDetail(): Unit = runBlocking {
        val data = AppConfig.dummyTvDetail

        val detail = repo.getTvDetail(null, data.show.id)
        verify(remoteSrc).getTvDetail(null, data.show.id)
        assert(detail is Success)
        val dataFromCall = (detail as Success).data
        assertEquals(tvDetail, dataFromCall)
    }
}
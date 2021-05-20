package sidev.app.course.dicoding.moviecatalog1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import sidev.app.course.dicoding.moviecatalog1.UnitTestingUtil.waitForValue
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowFavRepo
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.Dummy
import sidev.lib.`val`.SuppressLiteral

class ShowDetailViewModelTest {

    companion object {
        // These props are lazy val so there's no way the value will be changed after initialization.
        private val repo: ShowRepo by lazy { mock(ShowRepo::class.java) }
        //This is unused in this test context
        private val favRepo: ShowFavRepo by lazy { mock(ShowFavRepo::class.java) }

        private val movieDetail = Dummy.dummyMovieDetail
        private val tvDetail = Dummy.dummyTvDetail

        private const val dummyDeletedCount = 1
        private const val dummyFavBool = true
        private val dummyShowFav = movieDetail.show

        @BeforeClass
        @JvmStatic
        fun initSetup(): Unit = runBlocking {
            `when`(repo.getMovieDetail(any(), anyString())).thenReturn(
                Success(movieDetail)
            )
            `when`(repo.getTvDetail(any(), anyString())).thenReturn(
                Success(tvDetail)
            )

            `when`(favRepo.deleteFav(dummyShowFav)).thenReturn(dummyDeletedCount)
            `when`(favRepo.isShowFav(anyInt(), anyString())).thenReturn(dummyFavBool)
        }
    }

    private lateinit var vm: ShowDetailViewModel
    private lateinit var mockObserver: Observer<ShowDetail> //var cuz it's different Observer on every test case.
    private lateinit var mockFavObserver: Observer<Boolean> //var cuz it's different Observer on every test case.
    private lateinit var data: Show

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    fun setup(){
        mockObserver = mock(Observer::class.java) as Observer<ShowDetail>
        mockFavObserver = mock(Observer::class.java) as Observer<Boolean>
    }

    @Test
    fun downloadMovieDetail(): Unit = runBlocking {
        data = Dummy.dummyMovieItem
        vm = ShowDetailViewModel(null, repo, favRepo, Const.ShowType.MOVIE)
        vm.showDetail.observeForever(mockObserver)
        vm.downloadShowDetail(data.id)?.start()

        // Still need this line cuz Observer.onChanged() is invoked async.
        val dataFromCall = vm.showDetail.waitForValue()

        verify(repo).getMovieDetail(null, data.id)
        verify(mockObserver).onChanged(movieDetail)

        assertEquals(movieDetail, dataFromCall)
    }

    @Test
    fun downloadTvDetail(): Unit = runBlocking {
        data = Dummy.dummyTvItem
        vm = ShowDetailViewModel(null, repo, favRepo, Const.ShowType.TV)
        vm.showDetail.observeForever(mockObserver)
        vm.downloadShowDetail(data.id)?.start()

        // Still need this line cuz Observer.onChanged() is invoked async.
        val dataFromCall = vm.showDetail.waitForValue()

        verify(repo).getTvDetail(null, data.id)
        verify(mockObserver).onChanged(tvDetail)

        assertEquals(tvDetail, dataFromCall)
    }


    @Test
    fun insertFav(){
        vm = ShowDetailViewModel(null, repo, favRepo, Const.ShowType.MOVIE)
        vm.isFav.observeForever(mockFavObserver)
        vm.insertFav(dummyShowFav)

        val valFromLiveData = vm.isFav.waitForValue()

        verify(favRepo).insertFav(dummyShowFav)
        verify(mockFavObserver).onChanged(dummyFavBool)

        assertEquals(dummyFavBool, valFromLiveData)
    }

    @Test
    fun isFav(){
        vm = ShowDetailViewModel(null, repo, favRepo, Const.ShowType.MOVIE)
        vm.isFav.observeForever(mockFavObserver)
        vm.isFav(dummyShowFav.id)?.start()

        val valFromLiveData = vm.isFav.waitForValue()

        verify(favRepo).isShowFav(dummyShowFav.type, dummyShowFav.id)
        verify(mockFavObserver).onChanged(dummyFavBool)

        assertEquals(dummyFavBool, valFromLiveData)
    }

    @Test
    fun deleteFav(){
        vm = ShowDetailViewModel(null, repo, favRepo, Const.ShowType.MOVIE)
        vm.isFav.observeForever(mockFavObserver)
        vm.deleteFav(dummyShowFav)

        val valFromLiveData = vm.isFav.waitForValue()

        verify(favRepo).deleteFav(dummyShowFav)
        verify(mockFavObserver).onChanged(!dummyFavBool)

        assertEquals(!dummyFavBool, valFromLiveData)
    }
}
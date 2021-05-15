package sidev.app.course.dicoding.moviecatalog1.data.datasource

import android.content.Context
import sidev.app.course.dicoding.moviecatalog1.data.api.AppRetrofit
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.Util.get
import sidev.lib.`val`.SuppressLiteral
import sidev.app.course.dicoding.moviecatalog1.data.Result
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.Failure

object ShowRemoteRetrofitSource: ShowDataSource {
    override suspend fun getPopularMovieList(c: Context?): Result<List<Show>> = getPopularShowList(
        Const.ShowType.MOVIE
    )

    override suspend fun getPopularTvList(c: Context?): Result<List<Show>> = getPopularShowList(
        Const.ShowType.TV
    )

    override suspend fun getMovieDetail(c: Context?, id: String): Result<ShowDetail> = getShowDetail(
        id, Const.ShowType.MOVIE
    )

    override suspend fun getTvDetail(c: Context?, id: String): Result<ShowDetail> = getShowDetail(
        id, Const.ShowType.TV
    )


    private suspend fun getPopularShowList(type: Const.ShowType): Result<List<Show>> {
        val response = when(type) {
            Const.ShowType.MOVIE -> AppRetrofit.showApi.getPopularMovieList()
            Const.ShowType.TV -> AppRetrofit.showApi.getPopularTvList()
        }.get()

        return when(response){
            is Success -> Success(response.data.results)
            is Failure -> @Suppress(SuppressLiteral.UNCHECKED_CAST) {
                response as Failure<List<Show>>
            }
        }
    }

    private suspend fun getShowDetail(id: String, type: Const.ShowType): Result<ShowDetail> {
        return when(type) {
            Const.ShowType.MOVIE -> AppRetrofit.showApi.getMovieDetail(id)
            Const.ShowType.TV -> AppRetrofit.showApi.getTvDetail(id)
        }.get()
    }
}
package sidev.app.course.dicoding.moviecatalog1.data.api

import retrofit2.Retrofit
import sidev.app.course.dicoding.moviecatalog1.util.Const

object AppRetrofit {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Const.ENDPOINT_ROOT +"/")
        .addConverterFactory(ShowConverterFactory)
        .build()

    val showApi: ShowApi = retrofit.create(ShowApi::class.java)
}
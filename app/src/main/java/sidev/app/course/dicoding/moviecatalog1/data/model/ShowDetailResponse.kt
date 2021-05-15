package sidev.app.course.dicoding.moviecatalog1.data.model

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Converter
import sidev.app.course.dicoding.moviecatalog1.util.Util

/**
 * Only as mediator to [Show] data class. I won't change the structure of former [Show] data class for some reason:
 *  1. There won't be an error on former code.
 *  2. I'd prefer use composition approach to show relation between data classes.
 */
private data class ShowDetailResponse (
    val show: Show,
    val genres: List<Genre>,
    @SerializedName("runtime")
    val duration: Int?, //in minutes, null if the show type is tv show
    val tagline: String,
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropImg: String,
) {
    fun toDetail(show: Show? = null): ShowDetail = ShowDetail(
        show ?: this.show,
        genres.map { it.name },
        duration, tagline, overview, backdropImg
    )
}


object ShowDetailConverter: Converter<ResponseBody, ShowDetail> {
    override fun convert(value: ResponseBody): ShowDetail {
        val str = value.string()
        val detail = Util.gson.fromJson(str, ShowDetailResponse::class.java)
        val show = Util.gson.fromJson(str, Show::class.java)
        return detail.toDetail(show)
    }
}
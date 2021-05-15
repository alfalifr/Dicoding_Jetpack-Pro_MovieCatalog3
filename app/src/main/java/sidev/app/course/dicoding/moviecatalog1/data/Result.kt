package sidev.app.course.dicoding.moviecatalog1.data

sealed class Result<T>

data class Success<T>(val data: T): Result<T>()
data class Failure<T>(val code: Int, val e: Throwable?): Result<T>()
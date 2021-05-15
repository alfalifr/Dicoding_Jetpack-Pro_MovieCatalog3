package sidev.app.course.dicoding.moviecatalog1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sidev.app.course.dicoding.moviecatalog1.data.model.Show

@Database(entities = [Show::class], version = 1)
abstract class ShowFavDb: RoomDatabase() {
    abstract fun dao(): ShowFavDao

    companion object {
        private var instace: ShowFavDb?= null
        const val DB_NAME = "user_db"

        fun getInstance(ctx: Context): ShowFavDb {
            if(instace == null){
                synchronized(ShowFavDb::class){
                    instace = Room.databaseBuilder(
                        ctx,
                        ShowFavDb::class.java,
                        "$DB_NAME.db"
                    ).build()
                }
            }
            return instace!!
        }

        fun clearInstance(){
            instace = null
        }
    }
}
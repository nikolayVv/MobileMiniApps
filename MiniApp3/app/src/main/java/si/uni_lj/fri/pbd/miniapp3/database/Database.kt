package si.uni_lj.fri.pbd.miniapp3.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import si.uni_lj.fri.pbd.miniapp3.Constants
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import java.util.concurrent.Executors

@androidx.room.Database(entities = [RecipeDetails::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: Database? = null
        private const val NUMBER_OF_THREADS = 4
        // to access the db in the background
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)!!

        fun getDatabase(context: Context): Database? {
            if (INSTANCE == null) {
                synchronized(Database::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            Database::class.java, Constants.DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

    // TODO: add a DAO reference
    abstract fun recipeDao(): RecipeDao
}
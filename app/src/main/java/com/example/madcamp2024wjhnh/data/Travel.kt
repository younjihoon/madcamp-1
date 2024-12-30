package com.example.madcamp2024wjhnh.data
import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.lifecycle.LiveData
import androidx.room.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Parcelize
data class Travel(
    var id: Int = 0,
    var title: String,
    var place: String,
    var date: String,
    var tags: String,
    var memo: String,
    var thumbnail: Uri,
    var DayInfos: MutableList<DayInfo>
) : Parcelable

@Entity(tableName = "Travel")
data class TravelR(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary Key 추가
    var title: String,
    var place: String,
    var date: String,
    var tags: String,
    var memo: String,
    @TypeConverters(UriConverter::class) var thumbnail: Uri, // Uri 변환기 필요
    @TypeConverters(DayInfoListConverter::class) var DayInfos: MutableList<DayInfo> // 리스트 변환기 필요
)

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri): String = uri.toString()

    @TypeConverter
    fun toUri(uriString: String): Uri = Uri.parse(uriString)
}

class DayInfoListConverter {
    @TypeConverter
    fun fromDayInfoList(list: MutableList<DayInfo>): String = Gson().toJson(list)

    @TypeConverter
    fun toDayInfoList(json: String): MutableList<DayInfo> {
        val type = object : TypeToken<MutableList<DayInfo>>() {}.type
        return Gson().fromJson(json, type)
    }
}


@Dao
interface TravelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(travel: TravelR): Long

    @Update
    suspend fun update(travel: TravelR)

    @Delete
    suspend fun delete(travel: TravelR)

    @Query("SELECT * FROM Travel ORDER BY id ASC")
    fun getAllTravels(): LiveData<List<TravelR>>

    @Query("SELECT * FROM Travel WHERE id = :id")
    suspend fun getTravelById(id: Int): TravelR?
}



@Database(entities = [TravelR::class], version = 1, exportSchema = false)
@TypeConverters(UriConverter::class, DayInfoListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "travel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


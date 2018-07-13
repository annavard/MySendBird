import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.anna.mysendbird.db.Channel
import com.example.anna.mysendbird.db.ChannelDao

@Database(entities = [Channel::class], version = 1)
abstract class ChannelRoomDatabase : RoomDatabase() {

    abstract fun channelDao(): ChannelDao

    companion object {
        private var INSTANCE: ChannelRoomDatabase? = null

        fun getInstance(application: Application): ChannelRoomDatabase? {
            if (INSTANCE == null) {

                synchronized(ChannelRoomDatabase::class) {

                    INSTANCE = Room.databaseBuilder(application,
                            ChannelRoomDatabase::class.java,
                            "channels.db")
                            .build()
                }
            }

            return INSTANCE
        }
    }
}
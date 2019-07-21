package at.ict4d.ict4dnews.persistence.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import at.ict4d.ict4dnews.models.BLOG_TABLE_TABLE_NAME

class Migration1to2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.delete(BLOG_TABLE_TABLE_NAME, null, null) // resets the blogs table
    }
}
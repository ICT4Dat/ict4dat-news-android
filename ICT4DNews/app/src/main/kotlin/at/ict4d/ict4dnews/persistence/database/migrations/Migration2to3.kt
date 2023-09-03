package at.ict4d.ict4dnews.persistence.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from v2 to v3 as the detail screen was dropped and the description is not needed any more.
 */
class Migration2to3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE news")
        database.execSQL(
            """
                "CREATE TABLE IF NOT EXISTS news (
                `link` TEXT NOT NULL, 
                `author_id` TEXT, 
                `server_id` INTEGER NOT NULL, 
                `featured_media` TEXT, 
                `title` TEXT, 
                `published_date` TEXT, 
                `blog_id` TEXT, 
                PRIMARY KEY(`link`), 
                FOREIGN KEY(`author_id`) REFERENCES `authors`(`link`) 
                ON UPDATE NO ACTION ON DELETE NO ACTION , 
                FOREIGN KEY(`blog_id`) REFERENCES `blogs`(`blog_feed_url`) 
                ON UPDATE NO ACTION ON DELETE NO ACTION )"
            """
        )
    }
}

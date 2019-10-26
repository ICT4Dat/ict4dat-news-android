package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.FeedType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.ParseException
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class GsonLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        try {
            if (json?.asString.isNullOrEmpty()) {
                return null
            }

            return LocalDateTime.parse(json?.asString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        } catch (e: ParseException) {
            Timber.e(e, "Caught Parse Exception")
        }
        return null
    }
}

class GsonFeedTypeDeserializer : JsonDeserializer<FeedType> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): FeedType? {
        val intFeedType = json?.asInt
        if (intFeedType != null && intFeedType in 0..2) {
            return FeedType.values()[intFeedType]
        }
        return null
    }
}

package at.ict4d.ict4dnews.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.lang.reflect.Type
import java.text.ParseException

class GsonLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        try {
            return LocalDateTime.parse(json?.asString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        } catch (e: ParseException) {
            Timber.e("Caught Parse Exception", e)
        }
        return LocalDateTime.now()
    }
}

// TODO("Replace this class with actual class in future because ktlint complains class and file name is not matched")
class Temp
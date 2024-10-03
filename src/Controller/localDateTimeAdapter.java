package Controller;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


//thanks gpt
public class localDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Use the desired format

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value != null) {
            out.value(value.format(formatter));
        } else {
            out.nullValue();
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String str = in.nextString();
        return LocalDateTime.parse(str, formatter);
    }
}

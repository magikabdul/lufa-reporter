package cloud.cholewa.reporter.raport.lufa.mapper;

import cloud.cholewa.reporter.raport.lufa.model.TaskCategoryChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskChatResponseMapper {

    private final ObjectMapper objectMapper;

    public TaskCategoryChatResponse map(final String chatResponse) {
        try {
            return objectMapper.readValue(chatResponse, TaskCategoryChatResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response into TaskChatResponse. Raw response: {}", chatResponse, e);
            throw new IllegalArgumentException(
                "Invalid AI response format. Expected JSON with 'category' and 'description'.");
        }
    }
}

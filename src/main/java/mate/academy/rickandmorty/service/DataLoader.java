package mate.academy.rickandmorty.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import mate.academy.rickandmorty.entity.CharacterEntity;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@ConditionalOnProperty(
        name = "data.loader.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class DataLoader {
    private final CharacterRepository repository;
    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public DataLoader(CharacterRepository repository) {
        this.repository = repository;
        this.webClient = WebClient.builder()
                .baseUrl("https://rickandmortyapi.com")
                .build();
    }

    @PostConstruct
    public void loadData() {
        repository.deleteAll();

        String nextUrl = "/api/character";

        do {
            JsonNode root = (nextUrl.startsWith("http")
                    ? webClient.get().uri(URI.create(nextUrl))
                    : webClient.get().uri(nextUrl)
            )
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            for (JsonNode chr : root.get("results")) {
                CharacterEntity entity = new CharacterEntity();
                entity.setExternalId(chr.get("id").asText());
                entity.setName(chr.get("name").asText());
                entity.setStatus(chr.get("status").asText());
                entity.setGender(chr.get("gender").asText());
                repository.save(entity);
            }

            String candidate = root.get("info").get("next").asText(null);
            nextUrl = (candidate != null && !candidate.isEmpty()) ? candidate : null;

        } while (nextUrl != null);
    }
}

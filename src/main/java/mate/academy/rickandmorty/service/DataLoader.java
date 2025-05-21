package mate.academy.rickandmorty.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import mate.academy.rickandmorty.entity.CharacterEntity;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DataLoader {
    private final CharacterRepository repository;
    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DataLoader(CharacterRepository repository) {
        this.repository = repository;
        this.webClient = WebClient.create("https://rickandmortyapi.com/api/character");
    }

    @PostConstruct
    public void loadData() {
        repository.deleteAll();
        String url = "/";
        do {
            JsonNode root = webClient.get()
                    .uri(url)
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
            url = root.get("info").get("next").asText(null);
        } while (url != null && !url.isEmpty());
    }
}

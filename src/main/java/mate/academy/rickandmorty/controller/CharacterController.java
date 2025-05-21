package mate.academy.rickandmorty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import mate.academy.rickandmorty.dto.CharacterDto;
import mate.academy.rickandmorty.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "Characters", description = "Rick and Morty character operations")
public class CharacterController {

    private final CharacterService service;

    @Autowired
    public CharacterController(CharacterService service) {
        this.service = service;
    }

    @GetMapping("/random")
    @Operation(summary = "Get a random character")
    public CharacterDto random() {
        return service.getRandomCharacter();
    }

    @GetMapping("/search")
    @Operation(summary = "Search characters by name")
    public List<CharacterDto> search(@RequestParam String name) {
        return service.searchByName(name);
    }
}

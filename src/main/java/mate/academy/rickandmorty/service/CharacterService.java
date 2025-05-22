package mate.academy.rickandmorty.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import mate.academy.rickandmorty.dto.CharacterDto;
import mate.academy.rickandmorty.entity.CharacterEntity;
import mate.academy.rickandmorty.exception.NoCharactersFoundException;
import mate.academy.rickandmorty.mapper.CharacterMapper;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    private final CharacterRepository repository;
    private final Random random = new Random();

    @Autowired
    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    public CharacterDto getRandomCharacter() {
        List<CharacterEntity> all = repository.findAll();
        if (all.isEmpty()) {
            throw new NoCharactersFoundException("No characters found in the database.");
        }
        CharacterEntity picked = all.get(random.nextInt(all.size()));
        return CharacterMapper.toDto(picked);
    }

    public List<CharacterDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(CharacterMapper::toDto)
                .collect(Collectors.toList());
    }
}

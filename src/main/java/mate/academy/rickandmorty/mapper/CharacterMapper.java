package mate.academy.rickandmorty.mapper;

import mate.academy.rickandmorty.dto.CharacterDto;
import mate.academy.rickandmorty.entity.CharacterEntity;

public class CharacterMapper {
    public static CharacterDto toDto(CharacterEntity entity) {
        CharacterDto dto = new CharacterDto();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setGender(entity.getGender());
        return dto;
    }
}

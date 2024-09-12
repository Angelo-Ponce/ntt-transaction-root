package com.nttdata.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean("defaultMapper") // Se agrega el ("defaultMapper")
    public ModelMapper defaultMapper() {
        return new ModelMapper();
    }

    //Especificar como mapear los datos
    /*@Bean("medicMapper")
    public ModelMapper medicMapper() {
        ModelMapper modelMapper = new ModelMapper();

        //Escritura
        modelMapper.createTypeMap(MedicDTO.class, Medic.class)
                .addMapping(MedicDTO::getPrimaryName, (dest, v) -> dest.setFirstName((String) v))
                .addMapping(MedicDTO::getSurname, (dest, v) -> dest.setLastName((String) v))
                .addMapping(MedicDTO::getPhoto, (dest, v) -> dest.setPhotoUrl((String) v));
        // .addMapping(e -> e.getPrimaryName(), (dest, v) -> dest.setFirstName((String) v));

        //Lectura
        modelMapper.createTypeMap(Medic.class, MedicDTO.class)
                .addMapping(Medic::getFirstName, (dest, v) -> dest.setPrimaryName((String) v))
                .addMapping(Medic::getLastName, (dest, v) -> dest.setSurname((String) v));

        return modelMapper;
    }*/
}

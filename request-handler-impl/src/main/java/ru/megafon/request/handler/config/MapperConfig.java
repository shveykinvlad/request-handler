package ru.megafon.request.handler.config;

import ru.megafon.request.handler.model.Request;
import ru.megafon.request.handler.dto.RequestDto;
import ru.megafon.request.handler.dto.UserDto;
import ru.megafon.request.handler.model.User;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(RequestDto.class, Request.class)
                .byDefault()
                .register();

        mapperFactory.classMap(UserDto.class, User.class)
                .byDefault()
                .register();
    }
}

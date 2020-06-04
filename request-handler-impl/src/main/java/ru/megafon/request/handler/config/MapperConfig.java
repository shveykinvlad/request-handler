package ru.megafon.request.handler.config;

import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.context.annotation.Configuration;
import ru.megafon.request.handler.dto.RequestDto;
import ru.megafon.request.handler.dto.UserDto;
import ru.megafon.request.handler.model.Request;
import ru.megafon.request.handler.model.User;

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

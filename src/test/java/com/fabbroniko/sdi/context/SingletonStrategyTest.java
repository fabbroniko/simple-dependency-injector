package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.sdi.registry.Registry;
import com.fabbroniko.ul.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SingletonStrategyTest {

    @Mock
    private Logger logger;
    @Mock
    private ComponentFactory componentFactory;
    @Mock
    private Registry registry;
    @Mock
    private ApplicationContext applicationContext;

    // TODO
}
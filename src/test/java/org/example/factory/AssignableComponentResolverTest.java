package org.example.factory;

import org.example.naming.QualifierResolver;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AssignableComponentResolverTest {

    @Mock
    private Set<Class<?>> scannedComponents;
    @Mock
    private QualifierResolver nameResolver;
    @InjectMocks
    private AssignableComponentResolver componentResolver;
}
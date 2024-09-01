package org.example.target.context;

import org.example.annotation.Component;
import org.example.context.ApplicationContext;

@Component
public record DependsOnContext(ApplicationContext applicationContext) {
}

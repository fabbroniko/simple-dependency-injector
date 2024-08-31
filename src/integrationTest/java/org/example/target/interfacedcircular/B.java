package org.example.target.interfacedcircular;

import org.example.annotation.Component;

@Component
public record B(C c) implements InterfaceB {
}

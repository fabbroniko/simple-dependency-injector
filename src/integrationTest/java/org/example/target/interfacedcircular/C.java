package org.example.target.interfacedcircular;

import org.example.annotation.Component;

@Component
public record C(B b) {
}

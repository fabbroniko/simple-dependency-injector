package com.fabbroniko.sdi.target.interfacedcircular;

import com.fabbroniko.sdi.annotation.Component;

@Component
public record B(C c) implements InterfaceB {
}

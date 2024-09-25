package com.fabbroniko.sdi.target.interfacedcircular;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.interfacedcircular")
public record A(InterfaceB b) {
}

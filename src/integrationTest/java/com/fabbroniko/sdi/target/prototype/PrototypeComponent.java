package com.fabbroniko.sdi.target.prototype;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.sdi.annotation.Prototype;

@Component
@Prototype
@Configuration(componentScan = "com.fabbroniko.sdi.target.prototype")
public class PrototypeComponent {
}

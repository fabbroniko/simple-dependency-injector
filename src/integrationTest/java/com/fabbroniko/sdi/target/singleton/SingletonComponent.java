package com.fabbroniko.sdi.target.singleton;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;

@Component
@Configuration(componentScan = "com.fabbroniko.sdi.target.prototype")
public class SingletonComponent {
}

package com.fabbroniko.sdi.target.log;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.ul.Logger;

@Component
public record WithLoggerDependency(Logger logger) {
}

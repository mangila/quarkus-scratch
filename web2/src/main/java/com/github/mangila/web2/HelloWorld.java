package com.github.mangila.web2;

import io.quarkus.logging.Log;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class HelloWorld implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        Log.info("Hello World!");
        return 0;
    }
}

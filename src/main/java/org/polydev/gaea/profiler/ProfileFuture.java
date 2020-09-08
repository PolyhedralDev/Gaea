package org.polydev.gaea.profiler;

import java.util.concurrent.CompletableFuture;

public class ProfileFuture extends CompletableFuture<Boolean> {
    public ProfileFuture() {
        super();
    }

    public boolean complete() {
        return super.complete(true);
    }
}

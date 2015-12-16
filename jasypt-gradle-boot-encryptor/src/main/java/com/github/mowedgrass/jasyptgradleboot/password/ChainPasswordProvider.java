package com.github.mowedgrass.jasyptgradleboot.password;

import java.util.List;
import java.util.Optional;

public abstract class ChainPasswordProvider implements PasswordProvider {

    public static final String EMPTY_PASSWORD = "";

    @Override
    public final String getPassword() {
        return filter(getCandidates());
    }

    protected final String filter(List<Optional<String>> candidates) {
        return candidates.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst().orElse(EMPTY_PASSWORD);
    }

    abstract protected List<Optional<String>> getCandidates();
}

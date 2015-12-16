package com.github.mowedgrass.jasyptgradleboot.password;

public class DirectPasswordProvider implements PasswordProvider {

    private String password;

    public DirectPasswordProvider(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }
}

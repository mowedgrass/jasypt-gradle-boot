package com.github.mowedgrass.jasyptgradleboot.encryptor.configuration;

public enum ConfigurationProperty {

    ALGORITHM("jasypt.encryptor.algorithm", "PBEWithMD5AndDES"),
    KEY_OBTENTION_ITERATIONS("jasypt.encryptor.keyObtentionIterations", "1000"),
    POOL_SIZE("jasypt.encryptor.poolSize", "1"),
    PROVIDER_NAME("jasypt.encryptor.providerName", "SunJCE"),
    SALT_GENERATOR("jasypt.encryptor.saltGeneratorClassname", "org.jasypt.salt.RandomSaltGenerator"),
    OUTPUT_TYPE("jasypt.encryptor.stringOutputType", "base64");

    private String name;
    private String defaultValue;

    ConfigurationProperty(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }
}

package me.naming.delieveryservice.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.secret.password}")
    private String configPassword;

    @Value("${jasypt.algorithm}")
    private String jasyptAlgorithm;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(configPassword);             // 암, 복호화에 사용할 키
        config.setAlgorithm(jasyptAlgorithm);           // 암호화 알고리즘 지정
        config.setKeyObtentionIterations("1000");       // 암호화 키를 얻기 위해 적용된 해싱 반복 횟수를 설정한다.
        config.setPoolSize("1");                        // 암호기 생성하기 위한 pool 크기 지정.
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}

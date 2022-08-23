package com.homeloan.project.helpers;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class CustomIdGenerator implements IdentifierGenerator {

    public static final String generatorName = "customIdGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        int charLength = 8;
        return String.valueOf(new Random()
                        .nextInt(9 * (int) Math.pow(10, charLength - 1) - 1)
                        + (int) Math.pow(10, charLength - 1));

    }
}

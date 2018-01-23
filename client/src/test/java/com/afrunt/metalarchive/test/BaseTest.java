package com.afrunt.metalarchive.test;

import com.afrunt.metalarchive.impl.util.HttpClient;
import org.junit.Before;

/**
 * @author Andrii Frunt
 */
public abstract class BaseTest {
    @Before
    public void init() {
        System.setProperty(HttpClient.HTTP_CLIENT_TYPE, TestHttpClient.class.getName());
    }
}

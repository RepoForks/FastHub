package com.fastaccess.provider.rest.implementation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StringConverter extends Converter.Factory {

    @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new StringResponseConverter();
    }

    private static class StringResponseConverter implements Converter<ResponseBody, String> {
        @Override public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

}

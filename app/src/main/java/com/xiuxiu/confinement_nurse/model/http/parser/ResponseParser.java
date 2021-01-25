package com.xiuxiu.confinement_nurse.model.http.parser;

import com.xiuxiu.confinement_nurse.model.http.bean.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.parse.AbstractParser;

@Parser(name = "XXResponse")
public class ResponseParser<T> extends AbstractParser<T> {
    protected ResponseParser() {
    }

    public ResponseParser(@NotNull Type type) {
        super(type);
    }

    //省略构造方法
    @Override
    public T onParse(okhttp3.Response response) throws IOException {
        final Type type = ParameterizedTypeImpl.get(Response.class, mType); //获取泛型类型
        Response<T> data = convert(response, type);
        if (data == null) {
            throw new ParseException(String.valueOf(-1), "数据错误", response);
        }
        T t = data.getResults(); //获取data字段
        if (!data.isSuccess()) {
            throw new ParseException(String.valueOf(data.getErroCode()), data.getMessage(), response);
        } else {
            if (t == null && mType == String.class) {
                t = (T) "";
                return t;
            } else {
                return t;
            }
        }
    }
}


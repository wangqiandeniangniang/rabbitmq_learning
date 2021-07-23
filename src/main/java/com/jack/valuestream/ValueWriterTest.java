package com.jack.valuestream;

import com.rabbitmq.client.impl.ValueWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author liangchen
 * @date 2021/6/17
 */
public class ValueWriterTest {

    public static void main(String[] args) throws IOException {
        DataOutputStream out = new DataOutputStream(new ByteArrayOutputStream());
        ValueWriter valueWriter = new ValueWriter(out);
        valueWriter.writeShortstr("11");
    }
}

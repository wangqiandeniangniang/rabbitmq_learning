package com.jack.closeable;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author liangchen
 * @date 2021/6/4
 */
public class TestCloseable {
}
 class TryWithResource {

    public static void main(String[] args) {
        try (TestMe r = new TestMe()) {
            r.generalTest();
        } catch(Exception e) {
            System.out.println("From Exception Block");
        } finally {
            System.out.println("From Final Block");
        }
    }
}



 class TestMe implements Closeable {


    public void generalTest() {
        System.out.println(" GeneralTest ");
    }

     @Override
     public void close() throws IOException {
         System.out.println(" close ");

     }
 }
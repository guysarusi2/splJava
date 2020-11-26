package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
        System.out.println("moshe");
    }

    @Test
    public void testIsDone(){
        assertFalse(future.isDone());
        future.resolve("str");
        assertTrue(future.isDone());
    }

    @Test
    public void testGet(){
        assertFalse(future.isDone());
        future.resolve("str");
        assertEquals(future.get(),"str");
        assertTrue(future.isDone());
    }

    @Test
    public void testGetWithTime(){
        assertFalse(future.isDone());
        assertNull(future.get(500,TimeUnit.MILLISECONDS));
        assertFalse(future.isDone());
        future.resolve("str");
        assertEquals(future.get(500,TimeUnit.MILLISECONDS   ),"str");
        assertTrue(future.isDone());
    }

    @Test
    public void testResolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertEquals(future.get(),str);
    }
}

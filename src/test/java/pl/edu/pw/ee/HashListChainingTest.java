package pl.edu.pw.ee;

import org.junit.Test;
import pl.edu.pw.ee.services.HashTable;
import pl.edu.pw.ee.HashListChaining.Elem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

public class HashListChainingTest {

    private HashTable<Integer> hashTable;

    @Test
    public void addingTest(){
        hashTable = new HashListChaining<>(10);
        try {
            Random r = new Random();
            for(int i = 0; i < 20; i++){
                hashTable.add(r.nextInt(5));
            }

            Field lengthField = HashListChaining.class.getDeclaredField("nElem");
            lengthField.setAccessible(true);
            int length = lengthField.getInt(hashTable);
            assertEquals(20, length);

            Integer object = 10;
            int hashCode = object.hashCode();
            Field array = HashListChaining.class.getDeclaredField("hashElems");
            array.setAccessible(true);
            Method countIdMethod = HashListChaining.class.getDeclaredMethod("countHashId", int.class);
            countIdMethod.setAccessible(true);
            hashTable = new HashListChaining<>(5);
            int id = (int) countIdMethod.invoke(hashTable, hashCode);

            for(int i = 0; i < 5; i++){
                hashTable.add(object);
            }
            Object[] objects = (Object[]) array.get(hashTable);
            for(int i = 0; i < objects.length; i++){
                if(i == id){
                    assertNotNull(objects[i]);
                    Field nextElem = HashListChaining.Elem.class.getDeclaredField("next");
                    nextElem.setAccessible(true);
                    Elem<Object> elem = (Elem) objects[i];
                    Object o = nextElem.get(elem);
                    assertNull(o);
                } else {
                    assertNull(objects[i]);
                }
            }

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void gettingTest(){
        hashTable  = new HashListChaining<>(5);
        Integer object = 12;
        hashTable.add(object);
        Integer o = hashTable.get(object);
        assertNotNull(o);
        assertEquals(object, o);
    }

    @Test
    public void deletingTest(){
        int length = 20;
        int elements = 50;
        hashTable = new HashListChaining<>(20);
        List<Integer> list = new ArrayList<>(elements);
        Random r = new Random();
        for(int i = 0 ; i < elements; i++){
            int item = r.nextInt();
            list.add(item);
            hashTable.add(item);
        }
        for(int i = 0; i < elements; i++){
            Integer item = list.get(i);
            hashTable.delete(item);
            Object o = hashTable.get(item);
            assertNull(o);
        }
    }

    @Test
    public void specificDataTest(){
        assertThrows(IllegalArgumentException.class, ()->new HashListChaining<Float>(-1));
        HashTable<Integer> hashTable = new HashListChaining<>(2);
        assertThrows(IllegalArgumentException.class, ()->hashTable.add(null));
        assertThrows(IllegalArgumentException.class, ()->hashTable.get(null));
        assertThrows(IllegalArgumentException.class, ()->hashTable.delete(null));
    }
}

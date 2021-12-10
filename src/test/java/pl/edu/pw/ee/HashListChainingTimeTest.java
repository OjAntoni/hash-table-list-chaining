package pl.edu.pw.ee;

import org.junit.Test;
import pl.edu.pw.ee.services.HashTable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.fail;

public class HashListChainingTimeTest {

    @Test
    public void measureTime(){
        try(BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {

            List<String> words = new ArrayList<>();
            String word;
            while ((word =br.readLine()) != null ){
                words.add(word);
            }

            System.out.printf("%-3s %-11s %s\n", "№", "Hash size", "Average time [ns]");
            for(int i = 1; i<=7; i++){
                int tableLength = i*4096;
                List<Long> measuredTime = new ArrayList<>();
                HashTable<String> hashTable = new HashListChaining<>(tableLength);

                for (String w : words) {
                    hashTable.add(w);
                }

                for(int k = 0; k<30; k++){
                    long start = System.nanoTime();
                    for (String w : words) {
                        hashTable.get(w);
                    }
                    long end = System.nanoTime();
                    measuredTime.add(end-start);
                }

                measuredTime.sort(Comparator.naturalOrder());
                List<Long> subList = measuredTime.subList(10, 19);
                long time = 0;
                for (Long timeItem : subList) {
                    time += timeItem/10.0;
                }

                System.out.printf("%-3d %-11d %d\n", i, tableLength, time);
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}

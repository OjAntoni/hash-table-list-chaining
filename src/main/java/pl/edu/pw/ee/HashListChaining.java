package pl.edu.pw.ee;

import pl.edu.pw.ee.services.HashTable;

import java.lang.reflect.Array;

public class HashListChaining<T extends Comparable<T>> implements HashTable<T> {

    private final Elem<T> nullElement = null;
    private final Elem<T>[] hashElems;
    private int nElem;

    protected static class Elem<T> {
        private final T value;
        private Elem<T> next;

        Elem(T value, Elem<T> nextElem) {
            this.value = value;
            this.next = nextElem;
        }
    }

    public HashListChaining(int size) {
        if(size<=0){
            throw new IllegalArgumentException("Size for hash table can't be "+size);
        }
        hashElems = ((Elem<T>[]) Array.newInstance(Elem.class, size));
        initializeHash();
    }

    @Override
    public void add(T value) {
        if(value==null){
            throw new IllegalArgumentException("Value can't be null");
        }

        int hashCode = value.hashCode();
        int hashId = countHashId(hashCode);

        Elem<T> currentElem = hashElems[hashId];

        if(currentElem == nullElement){
            hashElems[hashId] = new Elem<>(value, nullElement);
            nElem++;
            return;
        }

        Elem<T> prev = currentElem;
        Elem<T> it = currentElem.next;

        if(prev.value.equals(value)){
            prev = new Elem<>(value, prev.next);
        } else {
            while (it!=null && !it.value.equals(value)){
                prev = it;
                it = it.next;
            }
            prev.next = new Elem<>(value, it == null ? null : it.next);
        }
        nElem++;
    }

    @Override
    public T get(T value) {
        if(value==null){
            throw new IllegalArgumentException("Value can't be null");
        }
        int hashCode = value.hashCode();
        int hashId = countHashId(hashCode);

        Elem<T> elem = hashElems[hashId];

        while (elem != nullElement && !elem.value.equals(value)) {
            elem = elem.next;
        }

        return elem != nullElement ?  elem.value : null;
    }

    public double countLoadFactor() {
        double size = hashElems.length;
        return nElem / size;
    }

    private void initializeHash() {
        int n = hashElems.length;
        for (int i = 0; i < n; i++) {
            hashElems[i] = nullElement;
        }
    }

    private int countHashId(int hashCode) {
        int n = hashElems.length;
        return Math.abs(hashCode) % n;
    }

    @Override
    public void delete(T value) {
        if(value==null){
            throw new IllegalArgumentException("Value can't be null");
        }
        int hashId = countHashId(value.hashCode());
        Elem<T> hashElem = hashElems[hashId];

        if(hashElem==null){
            return;
        }

        Elem<T> prev = hashElem;
        Elem<T> it  = hashElem.next;
        if(prev.value.equals(value)){
            hashElems[hashId] = it;
        } else {
            while (it!=null && !it.value.equals(value)){
                prev = it;
                it = it.next;
            }
            prev.next = it == null ? null : it.next;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Elem<T> hashElem : hashElems) {
            sb.append(hashElem==null ? "null" : hashElem.value.hashCode()).append("->");
            if(hashElem!=null){
                Elem<T> it = hashElem.next;
                while (it!=null){
                    sb.append(it.value.hashCode()).append("->");
                    it = it.next;
                }
                sb.append("null");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
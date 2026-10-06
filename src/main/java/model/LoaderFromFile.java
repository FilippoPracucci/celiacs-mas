package model;

public interface LoaderFromFile<T> {

    T load(String filePath);
}

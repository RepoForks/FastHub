
package com.fastaccess.provider.paperdb;

import android.content.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.paperdb.Book;
import io.paperdb.Paper;
import rx.Completable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Adapter class with a new interface to perform PaperDB operations.
 *
 * @author pakoito
 */
public class RxPaperBook {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    private final Book book;

    private RxPaperBook() {
        book = Paper.book();
    }

    private RxPaperBook(String customBook) {
        book = Paper.book(customBook);
    }

    public Book getBook() {
        return book;
    }

    /**
     * Initializes the underlying {@link Paper} database.
     * <p/>
     * This operation is required only once, but can be called multiple times safely.
     *
     * @param context
     *         application context
     */
    public static void init(Context context) {
        if (INITIALIZED.compareAndSet(false, true)) {
            Paper.init(context.getApplicationContext());
        }
    }

    private static void assertInitialized() {
        if (!INITIALIZED.get()) {
            throw new IllegalStateException(
                    "RxPaper not initialized. Call RxPaper#init(Context) once");
        }
    }

    /**
     * Open the main {@link Book} running its operations on {@link Schedulers#io()}.
     * <p/>
     * Requires calling {@link RxPaperBook#init(Context)} at least once beforehand.
     *
     * @return new RxPaperBook
     */
    public static RxPaperBook with() {
        assertInitialized();
        return new RxPaperBook();
    }

    /**
     * Open a custom {@link Book} running its operations on {@link Schedulers#io()}.
     * <p/>
     * Requires calling {@link RxPaperBook#init(Context)} at least once beforehand.
     *
     * @param customBook
     *         book name
     * @return new RxPaperBook
     */
    public static RxPaperBook with(String customBook) {
        assertInitialized();
        return new RxPaperBook(customBook);
    }

    /**
     * Saves most types of POJOs or collections in {@link Book} storage.
     * <p/>
     * To deserialize correctly it is recommended to have an all-args constructor, but other types
     * may be available.
     *
     * @param key
     *         object key is used as part of object's file name
     * @param value
     *         object to save, must have no-arg constructor, can't be null.
     * @return this Book instance
     */
    public <T> Completable write(final String key, final T value) {
        return Completable
                .fromAction(() -> book.write(key, value))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Instantiates saved object using original object class (e.g. LinkedList). Support limited
     * backward and forward compatibility: removed fields are ignored, new fields have their default
     * values.
     *
     * @param key
     *         object key to read
     * @param defaultValue
     *         value to be returned if key doesn't exist
     * @return the saved object instance or defaultValue
     */
    public <T> Single<T> read(final String key, final T defaultValue) {
        return Single
                .fromCallable((Func0<T>) () -> book.read(key, defaultValue))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Instantiates saved object using original object class (e.g. LinkedList). Support limited
     * backward and forward compatibility: removed fields are ignored, new fields have their default
     * values.
     *
     * @param key
     *         object key to read
     * @return the saved object instance
     */
    public <T> Single<T> read(final String key) {
        return Single
                .fromCallable((Func0<T>) () -> book.read(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Delete saved object for given key if it is exist.
     */
    public Completable delete(final String key) {
        return Completable
                .fromAction(() -> book.delete(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Check if an object with the given key is saved in Book storage.
     *
     * @param key
     *         object key
     * @return true if object with given key exists in Book storage, false otherwise
     */
    public Single<Boolean> exists(final String key) {
        return Single
                .fromCallable((Func0<Boolean>) () -> book.exist(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns all keys for objects in {@link Book}.
     *
     * @return all keys
     */
    public Single<List<String>> keys() {
        return Single
                .fromCallable((Func0<List<String>>) book::getAllKeys)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Destroys all data saved in {@link Book}.
     */
    public Completable destroy() {
        return Completable
                .fromAction(book::destroy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
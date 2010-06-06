package com.github.srec.command.value;

/**
 * A value.
 *
 * @author Victor Tatai
 */
public abstract class Value <T> {
    protected Type type;
    protected T value;

    public Value(Type type) {
        this.type = type;
    }

    public Value(Type type, T value) {
        this.value = value;
        this.type = type;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

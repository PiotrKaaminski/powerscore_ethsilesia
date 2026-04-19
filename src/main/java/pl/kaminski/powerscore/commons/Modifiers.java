package pl.kaminski.powerscore.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Modifiers {
    private final List<ModifierEntry<?>> modifiers = new ArrayList<>();

    public <T> void add(T value, Consumer<T> modifier) {
        modifiers.add(new ModifierEntry<>(value, modifier));
    }

    public void modifyAll() {
        modifiers.forEach(ModifierEntry::modify);
    }

    private record ModifierEntry<T>(
            T value,
            Consumer<T> modifier) {
        private void modify() {
            modifier.accept(value);
        }
    }
}

package com.tsarev.githint.common;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * Naive and exception/concurrent unsafe list proxy to implement {@link javax.swing.ListModel}.
 * No iterator elements modification supported.
 * </p>
 * <p>
 * Useful, when iterator or concurrent modifications are not needed.
 * </p>
 */
public class ObservableListProxy<T> extends AbstractListModel<T> implements List<T> {

    /**
     * Proxied list.
     */
    private final List<T> innerCollection;

    /**
     * Constructor.
     */
    public ObservableListProxy(List<T> innerCollection) {
        this.innerCollection = innerCollection;
    }


    @Override
    public int size() {
        return innerCollection.size();
    }

    @Override
    public boolean isEmpty() {
        return innerCollection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerCollection.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private final Iterator<T> innerIterator = innerCollection.iterator();

            @Override
            public boolean hasNext() {
                return innerIterator.hasNext();
            }

            @Override
            public T next() {
                return innerIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return innerCollection.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return innerCollection.toArray(a);
    }

    @Override
    public boolean add(T t) {
        int size = innerCollection.size();
        try {
            return innerCollection.add(t);
        } finally {
            fireIntervalAdded(this, size, size);
        }
    }

    @Override
    public boolean remove(Object o) {
        int found = innerCollection.indexOf(o);
        if (found == -1) {
            return false;
        } else {
            innerCollection.remove(o);
            fireIntervalRemoved(this, found, found);
            return true;
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return innerCollection.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        int size = innerCollection.size();
        int newTail = c.size() + size - 1;
        try {
            return innerCollection.addAll(c);
        } finally {
            fireIntervalAdded(this, size, newTail);
        }
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        int addedSize = c.size();
        try {
            return innerCollection.addAll(index, c);
        } finally {
            fireIntervalAdded(this, index, addedSize + index);
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean changed = false;
        // Naive iteration to simplify listeners notifying.
        for (Object element : c) {
            changed |= this.remove(element);
        }
        return changed;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean changed = false;
        // Naive iteration to simplify listeners notifying.
        for (Object element : this) {
            if (!c.contains(element)) {
                changed |= this.remove(element);
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        int size = this.size();
        if (size == 0) {
            return;
        }
        innerCollection.clear();
        this.fireIntervalRemoved(this, 0, size - 1);
    }

    @Override
    public T get(int index) {
        return innerCollection.get(index);
    }

    @Override
    public T set(int index, T element) {
        try {
            return innerCollection.set(index, element);
        } finally {
            this.fireContentsChanged(this, index, index);
        }
    }

    @Override
    public void add(int index, T element) {
        innerCollection.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public T remove(int index) {
        try {
            return innerCollection.remove(index);
        } finally {
            this.fireIntervalRemoved(this, index, index);
        }
    }

    @Override
    public int indexOf(Object o) {
        return innerCollection.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerCollection.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return new NonModifiableListIteratorProxy<>(new NonModifiableListIteratorProxy<>(innerCollection.listIterator()));
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return new NonModifiableListIteratorProxy<>(new NonModifiableListIteratorProxy<>(innerCollection.listIterator(index)));
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new ObservableListProxy<>(innerCollection.subList(fromIndex, toIndex));
    }

    @Override
    public int getSize() {
        return innerCollection.size();
    }

    @Override
    public T getElementAt(int index) {
        return this.get(index);
    }

    /**
     * Non modifiable proxy, that throws {@link UnsupportedOperationException}.
     */
    private static class NonModifiableListIteratorProxy<T> implements ListIterator<T> {

        private final ListIterator<T> innerIterator;

        private NonModifiableListIteratorProxy(ListIterator<T> innerIterator) {
            this.innerIterator = innerIterator;
        }

        @Override
        public boolean hasNext() {
            return innerIterator.hasNext();
        }

        @Override
        public T next() {
            return innerIterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return innerIterator.hasPrevious();
        }

        @Override
        public T previous() {
            return innerIterator.previous();
        }

        @Override
        public int nextIndex() {
            return innerIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return innerIterator.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}

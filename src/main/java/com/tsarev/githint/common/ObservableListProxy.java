package com.tsarev.githint.common;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * Naive and exception/concurrent unsafe list proxy to implement some swing model.
 * No iterator elements modification supported.
 * </p>
 * <p>
 * Useful, when iterator or concurrent modifications are not needed.
 * </p>
 */
public abstract class ObservableListProxy<DataT, ModelT> implements List<DataT> {

    /**
     * Proxied list.
     */
    private final List<DataT> innerList;

    /**
     * Callback, called when elements are added.
     */
    private final ModelCallback<ModelT> addCallback;

    /**
     * Callback, called when elements are removed.
     */
    private final ModelCallback<ModelT> removeCallback;

    /**
     * Callback, called when elements are changed.
     */
    private final ModelCallback<ModelT> changeCallback;

    /**
     * Constructor.
     *
     * @param innerList proxied modifiable collection
     * @param addCallback add element callback
     * @param removeCallback remove element callback
     * @param changeCallback change element callback
     */
    public ObservableListProxy(List<DataT> innerList,
                               ModelCallback<ModelT> addCallback,
                               ModelCallback<ModelT> removeCallback,
                               ModelCallback<ModelT> changeCallback) {
        this.innerList = innerList;
        this.addCallback = addCallback;
        this.removeCallback = removeCallback;
        this.changeCallback = changeCallback;
    }

    /**
     * Get inner model.
     */
    protected abstract ModelT getModel();

    /**
     * Method to get sublist from proxied collection.
     */
    protected final List<DataT> innerSubList(int from, int to) {
        return innerList.subList(from, to);
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @NotNull
    @Override
    public Iterator<DataT> iterator() {
        return new Iterator<DataT>() {

            private final Iterator<DataT> innerIterator = innerList.iterator();

            @Override
            public boolean hasNext() {
                return innerIterator.hasNext();
            }

            @Override
            public DataT next() {
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
        return innerList.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return innerList.toArray(a);
    }

    @Override
    public boolean add(DataT dataT) {
        int size = innerList.size();
        try {
            return innerList.add(dataT);
        } finally {
            addCallback.doNotifyModel(getModel(), size, size);
        }
    }

    @Override
    public boolean remove(Object o) {
        int found = innerList.indexOf(o);
        if (found == -1) {
            return false;
        } else {
            innerList.remove(o);
            removeCallback.doNotifyModel(getModel(), found, found);
            return true;
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return innerList.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends DataT> c) {
        int size = innerList.size();
        int newTail = c.size() + size - 1;
        try {
            return innerList.addAll(c);
        } finally {
            addCallback.doNotifyModel(getModel(), size, newTail);
        }
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends DataT> c) {
        int addedSize = c.size();
        try {
            return innerList.addAll(index, c);
        } finally {
            addCallback.doNotifyModel(getModel(), index, addedSize + index);
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
        innerList.clear();
        removeCallback.doNotifyModel(getModel(), 0, size - 1);
    }

    @Override
    public DataT get(int index) {
        return innerList.get(index);
    }

    @Override
    public DataT set(int index, DataT element) {
        try {
            return innerList.set(index, element);
        } finally {
            changeCallback.doNotifyModel(getModel(), index, index);
        }
    }

    @Override
    public void add(int index, DataT element) {
        innerList.add(index, element);
        addCallback.doNotifyModel(getModel(), index, index);
    }

    @Override
    public DataT remove(int index) {
        try {
            return innerList.remove(index);
        } finally {
            removeCallback.doNotifyModel(getModel(), index, index);
        }
    }

    @Override
    public int indexOf(Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerList.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<DataT> listIterator() {
        return new NonModifiableListIteratorProxy<>(new NonModifiableListIteratorProxy<>(innerList.listIterator()));
    }

    @NotNull
    @Override
    public ListIterator<DataT> listIterator(int index) {
        return new NonModifiableListIteratorProxy<>(new NonModifiableListIteratorProxy<>(innerList.listIterator(index)));
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

    /**
     * Interface to separate custom swing model from list proxy.
     */
    @FunctionalInterface
    public interface ModelCallback<ModelT> {
        /**
         * Notify model about changes.
         */
        void doNotifyModel(ModelT model, Integer beginIndex, Integer endIndex);
    }
}

package org.dolan.searcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchCache.
 */
public class SearchCache implements ISearchCache<String> {

	/** The list. */
	private List<String> list;
	
	/** The size of the search cache. */
	private int size;

	/**
	 * Instantiates a new search cache.
	 */
	public SearchCache() {
		this.list = new ArrayList<String>();
	}

	/**
	 * Instantiates a new search cache.
	 *
	 * @param size the size
	 */
	public SearchCache(int size) {
		this.list = new ArrayList<String>();
		this.size = size;
	}

	/**
	 * Gets the list iterator.
	 *
	 * @return the list iterator
	 */
	public ListIterator<String> getListIterator() {
		return list.listIterator(list.size());
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearchCache#setCapacity(int)
	 */
	@Override
	public void setCapacity(int size) {
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearchCache#get(int)
	 */
	@Override
	public String get(int index) {
		return list.get(index);
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearchCache#getFromEnd(int)
	 */
	@Override
	public String getFromEnd(int index) {
		return get(this.list.size() - 1 - index);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends String> arg0) {
		return list.addAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		list.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object arg0) {
		return list.contains(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> arg0) {
		return list.containsAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return list.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object arg0) {
		return list.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> arg0) {
		return list.removeAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> arg0) {
		return list.retainAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] arg0) {
		return list.toArray(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#add(java.lang.Object)
	 */
	@Override
	public boolean add(String arg0) {
		boolean result = list.add(arg0);
		if (this.size() > this.size)
			this.poll();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#element()
	 */
	@Override
	public String element() {
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(String arg0) {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#peek()
	 */
	@Override
	public String peek() {
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#poll()
	 */
	@Override
	public String poll() {
		String output = list.get(0);
		list.remove(0);
		return output;
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#remove()
	 */
	@Override
	public String remove() {
		return list.remove(0);
	}

}

package com.victorguerrero.screeninteractioners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactsController implements ScreenInteractionContactFetcher.OnFetched {
	private ArrayList<Contact> contacts;
	private static ContactsController instance = null;
	private static final String SCREEN_INTERACTION_CONTACTS_URL = "http://www.screeninteraction.com/~simonedstrom/worksample-android/contacts.json";
	
	public enum Ordering { NAME_INCREASING, NAME_DECREASING, SURNAME_INCREASING, SURNAME_DECREASING};
	private Ordering ordering;
	
	public interface OnContactsChangeListener {
		public void onContactsChange();
	}
	
	private OnContactsChangeListener contactsChangeListener;
	
	private ContactsController() {
		contacts = new ArrayList<Contact>();
	    refresh();
	}
	
	public static ContactsController getInstance() {
		if (instance == null) {
			instance = new ContactsController();
		}
		
		return instance;
	}
	
	public final ArrayList<Contact> get() {
		return contacts;
	}
	
	public void refresh() {
		new ScreenInteractionContactFetcher()
			.setOnFetched(this)
			.execute(SCREEN_INTERACTION_CONTACTS_URL);
	}
	
	public void update(ArrayList<Contact> contacts) {
		updateContacts(contacts);
		orderContacts();
		if (contactsChangeListener != null) {
			contactsChangeListener.onContactsChange();
		}
	}

	public void setOnContactsChangeListener(OnContactsChangeListener listener) {
		contactsChangeListener = listener;
	}
	
	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
		update(this.contacts);
	}
	
	public void updateContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public void orderContacts() {
		final int isSurname;
		final int decreasing;
		
		if (ordering == Ordering.NAME_DECREASING || ordering == Ordering.NAME_INCREASING) {
			isSurname = 0;
		} else {
			isSurname = 1;
		}
		
		if (ordering == Ordering.NAME_DECREASING || ordering == Ordering.SURNAME_DECREASING) {
			decreasing = -1;
		} else {
			decreasing = 1;
		}
		
		Collections.sort(contacts, new Comparator<Contact>() {
		    public int compare(Contact a, Contact b) {
		    	String[] nameA = a.getName().split(" ");
		    	String[] nameB = b.getName().split(" ");
		    	
		        return decreasing*compareNames(isSurname, nameA, nameB);
		    }
		    
		    public int compareNames(int start, String[] nameA, String[] nameB) {
	    		int limit = Math.min(nameA.length, nameB.length);
		    	
		    	for (int i = start; i < limit; ++i) {
		    		int comp = nameA[i].compareTo(nameB[i]);
		    		if (comp != 0) {
		    			return comp;
		    		}
		    	}
		    	
		    	return nameA[0].compareTo(nameB[0]);
		    }
		});
	}
	
	public Contact getContact(int order) {
		return contacts.get(order);
	}
	
	public void setOrdering(int ordering) {
		ContactsController.Ordering order = ContactsController.Ordering.values()[ordering];
		
		setOrdering(order);
	}

	@Override
	public <T> void onFetched(T contacts) {
		this.update((ArrayList<Contact>)contacts);
	}
}

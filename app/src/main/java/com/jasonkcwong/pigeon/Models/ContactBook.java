package com.jasonkcwong.pigeon.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jason on 16-07-31.
 */
public class ContactBook{
    private List<Contact> contacts;

    public ContactBook(){
        contacts = new ArrayList<>();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact){
        contacts.add(contact);
    }

    public void sortContact(){

        Collections.sort(contacts, new ContactComparator());
    }

    private class ContactComparator implements Comparator<Contact> {
        @Override
        public int compare(Contact contact1, Contact contact2) {
            return contact1.getDisplayName().compareTo(contact2.getDisplayName());
        }
    }

}

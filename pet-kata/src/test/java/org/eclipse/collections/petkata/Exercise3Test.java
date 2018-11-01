/*
 * Copyright (c) 2017 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.petkata;

import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * In the slides leading up to this exercise you should have learned about the following APIs.
 * <p/>
 * {@link Bag}<br>
 * {@link MutableBag}<br>
 * {@link MutableList#toBag()}<br>
 * <br>
 * {@link Multimap}<br>
 * {@link MutableList#groupBy(Function)}<br>
 * {@link MutableList#groupByEach(Function)}<br>
 * {@link Multimaps}<br>
 *
 * @see <a href="http://eclipse.github.io/eclipse-collections-kata/pet-kata/#/6">Exercise 3 Slides</a>
 */
public class Exercise3Test extends PetDomainForKata
{
    @Test
    public void getCountsByPetType()
    {
        // Hint: use the appropriate method on this.people to create a Bag<PetType>
        Bag<PetType> counts = this.people.flatCollect(Person::getPets).countBy(Pet::getType);
        Assert.assertEquals(2, counts.occurrencesOf(PetType.CAT));
        Assert.assertEquals(2, counts.occurrencesOf(PetType.DOG));
        Assert.assertEquals(2, counts.occurrencesOf(PetType.HAMSTER));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.SNAKE));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.TURTLE));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.BIRD));
    }

    @Test
    public void getPeopleByLastName()
    {
        // Hint: use the appropriate method on this.people to create a Multimap<String, Person>
        Multimap<String, Person> byLastNameMultimap = this.people.groupBy(Person::getLastName);

        Verify.assertIterableSize(3, byLastNameMultimap.get("Smith"));
    }

    @Test
    public void getPeopleByTheirPets()
    {
        // Hint: use the appropriate method on this.people with a target collection to create a MutableSetMultimap<String, Person>
        // Hint: this.people is a MutableList, so it will return a MutableListMultimap without a target collection
        MutableSetMultimap<PetType, Person> multimap =
                this.people.groupByEach(Person::getPetTypes, Multimaps.mutable.set.empty());

        Verify.assertIterableSize(2, multimap.get(PetType.CAT));
        Verify.assertIterableSize(2, multimap.get(PetType.DOG));
        Verify.assertIterableSize(1, multimap.get(PetType.HAMSTER));
        Verify.assertIterableSize(1, multimap.get(PetType.TURTLE));
        Verify.assertIterableSize(1, multimap.get(PetType.BIRD));
        Verify.assertIterableSize(1, multimap.get(PetType.SNAKE));
    }
}

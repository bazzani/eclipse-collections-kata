/*
 * Copyright (c) 2016 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.petkata;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.IntSet;
import org.eclipse.collections.api.set.primitive.MutableBooleanSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.block.factory.primitive.IntPredicates;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.factory.primitive.BooleanSets;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.eclipse.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * In this set of tests, wherever you see .stream() replace it with an Eclipse Collections alternative.
 */
public class Exercise4Test extends PetDomainForKata
{
    @Test
    public void getAgeStatisticsOfPets()
    {
        // Try to use a MutableIntList here instead
        // Hints: flatMap = flatCollect, map = collect, mapToInt = collectInt
        IntList petAges = this.people
                .asLazy()
                .flatCollect(Person::getPets)
                .collectInt(Pet::getAge)
                .toList();

        // Try to use an IntSet here instead
        IntSet uniqueAges = petAges.toSet();

        // IntSummaryStatistics is a class in JDK 8 - Try and use it with MutableIntList.forEach()
        IntSummaryStatistics stats = new IntSummaryStatistics();
        petAges.forEach(stats::accept);

        // Is a Set<Integer> equal to an IntSet?
        // Hint: Try IntSets instead of Sets as the factory
        Assert.assertEquals(IntHashSet.newSetWith(1, 2, 3, 4), uniqueAges);

        // Try to leverage min, max, sum, average from the Eclipse Collections primitive api
        Assert.assertEquals(stats.getMin(), petAges.min());
        Assert.assertEquals(stats.getMax(), petAges.max());
        Assert.assertEquals(stats.getSum(), petAges.sum());
        Assert.assertEquals(stats.getAverage(), petAges.average(), 0.0);
        Assert.assertEquals(stats.getCount(), petAges.size());

        // Hint: Match = Satisfy
        Assert.assertTrue(petAges.allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertTrue(petAges.allSatisfy(i -> i > 0));
        Assert.assertFalse(petAges.anySatisfy(i -> i == 0));
        Assert.assertTrue(petAges.noneSatisfy(i -> i < 0));
        Assert.assertEquals(2.0d, petAges.median(), 0.0);
    }

    @Test
    public void streamsToECRefactor1()
    {
        //find Bob Smith
        Person person = this.people
                .detect(p -> p.named("Bob Smith"));

        //get Bob Smith's pets' names
        String names = person.getPets()
                .collect(Pet::getName).makeString(" & ");

        Assert.assertEquals("Dolly & Spot", names);
    }

    @Test
    public void streamsToECRefactor2()
    {
        // Hint: Try to replace the Map<PetType, Long> with a Bag<PetType>
        Map<PetType, Long> countsStream =
                Collections.unmodifiableMap(
                        this.people.stream()
                                .flatMap(person -> person.getPets().stream())
                                .collect(Collectors.groupingBy(Pet::getType,
                                        Collectors.counting())));
        Assert.assertEquals(Long.valueOf(2L), countsStream.get(PetType.CAT));
        Assert.assertEquals(Long.valueOf(2L), countsStream.get(PetType.DOG));
        Assert.assertEquals(Long.valueOf(2L), countsStream.get(PetType.HAMSTER));
        Assert.assertEquals(Long.valueOf(1L), countsStream.get(PetType.SNAKE));
        Assert.assertEquals(Long.valueOf(1L), countsStream.get(PetType.TURTLE));
        Assert.assertEquals(Long.valueOf(1L), countsStream.get(PetType.BIRD));

        // Don't forget to comment this out or delete it when you are done
        Assert.fail("Refactor to Eclipse Collections");
    }

    /**
     * The purpose of this test is to determine the top 3 pet types.
     */
    @Test
    public void streamsToECRefactor3()
    {
        // Hint: The result of groupingBy/counting can almost always be replaced by a Bag
        // Hint: Look for the API on Bag that might return the top 3 pet types
        List<Map.Entry<PetType, Long>> favoritesStream =
                this.people.stream()
                        .flatMap(p -> p.getPets().stream())
                        .collect(Collectors.groupingBy(Pet::getType, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .sorted(Comparator.comparingLong(e -> -e.getValue()))
                        .limit(3)
                        .collect(Collectors.toList());
        Verify.assertSize(3, favoritesStream);
        Verify.assertContains(new AbstractMap.SimpleEntry<>(PetType.CAT, Long.valueOf(2)), favoritesStream);
        Verify.assertContains(new AbstractMap.SimpleEntry<>(PetType.DOG, Long.valueOf(2)), favoritesStream);
        Verify.assertContains(new AbstractMap.SimpleEntry<>(PetType.HAMSTER, Long.valueOf(2)), favoritesStream);

        // Don't forget to comment this out or delete it when you are done
        Assert.fail("Refactor to Eclipse Collections");
    }
}

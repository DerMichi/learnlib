/* Copyright (C) 2013 TU Dortmund
 This file is part of LearnLib

 LearnLib is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License version 3.0 as published by the Free Software Foundation.

 LearnLib is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with LearnLib; if not, see
 <http://www.gnu.de/documents/lgpl.en.html>.  */
package de.learnlib.oracles;

import de.learnlib.api.MembershipOracle;
import de.learnlib.api.Query;
import de.learnlib.statistics.HistogramDataSet;
import de.learnlib.statistics.StatisticOracle;
import java.util.Collection;

/**
 * Collects a histogram of passed query lengths.
 *
 * @param <I> input symbol class
 * @param <O> output symbol class
 *
 * @author falkhowar
 */
public class HistogramOracle<I, O> implements StatisticOracle<I, O> {

    /**
     * dataset to be collected.
     */
    private final HistogramDataSet dataSet;

    /**
     * oracle used to answer queries.
     */
    private MembershipOracle<I, O> nextOracle;

    /**
     *
     * @param next real oracle
     * @param name name of the collected data set
     */
    public HistogramOracle(final MembershipOracle<I, O> next, final String name) {
	this.nextOracle = next;
	this.dataSet = new HistogramDataSet(name, "query length");
    }

    @Override
    public final void processQueries(final Collection<? extends Query<I, O>> queries) {
	for (Query<I, O> q : queries) {
	    this.dataSet.addDataPoint((long) q.getInput().size());
	}
	nextOracle.processQueries(queries);
    }

    /**
     * @return the data set collected by this oracle.
     */
    @Override
    public final HistogramDataSet getStatisticalData() {
	return this.dataSet;
    }

    /**
     * set used oracle.
     *
     * @param next oracle to be used
     */
    @Override
    public final void setNext(final MembershipOracle<I, O> next) {
	this.nextOracle = next;
    }
}

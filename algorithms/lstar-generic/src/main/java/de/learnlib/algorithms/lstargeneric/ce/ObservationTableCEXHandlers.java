/* Copyright (C) 2013 TU Dortmund
 * This file is part of LearnLib, http://www.learnlib.de/.
 * 
 * LearnLib is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 3.0 as published by the Free Software Foundation.
 * 
 * LearnLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with LearnLib; if not, see
 * <http://www.gnu.de/documents/lgpl.en.html>.
 */
package de.learnlib.algorithms.lstargeneric.ce;

import java.util.Collections;
import java.util.List;

import net.automatalib.automata.concepts.SuffixOutput;
import net.automatalib.words.Word;
import de.learnlib.algorithms.lstargeneric.table.ObservationTable;
import de.learnlib.algorithms.lstargeneric.table.Row;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.Query;
import de.learnlib.counterexamples.GlobalSuffixFinder;
import de.learnlib.counterexamples.GlobalSuffixFinders;
import de.learnlib.counterexamples.LocalSuffixFinder;
import de.learnlib.counterexamples.LocalSuffixFinders;
import de.learnlib.oracles.DefaultQuery;

public class ObservationTableCEXHandlers {
	
	public static ObservationTableCEXHandler<Object,Object> CLASSIC_LSTAR
		= new ObservationTableCEXHandler<Object, Object>() {
			@Override
			public <RI,RO> List<List<Row<RI>>> handleCounterexample(
					DefaultQuery<RI, RO> ceQuery,
					ObservationTable<RI, RO> table,
					SuffixOutput<RI, RO> hypOutput,
					MembershipOracle<RI, RO> oracle) {
				return handleClassicLStar(ceQuery, table, oracle);
			}
			@Override
			public boolean needsConsistencyCheck() {
				return true;
			}
		};
		
	public static ObservationTableCEXHandler<Object,Object> SUFFIX1BY1
		= new ObservationTableCEXHandler<Object,Object>() {
			@Override
			public <RI,RO>
			List<List<Row<RI>>> handleCounterexample(
					DefaultQuery<RI, RO> ceQuery,
					ObservationTable<RI, RO> table,
					SuffixOutput<RI, RO> hypOutput,
					MembershipOracle<RI, RO> oracle) {
				return handleSuffix1by1(ceQuery, table, oracle);
			}
			@Override
			public boolean needsConsistencyCheck() {
				return false;
			}
		};
		
	public static ObservationTableCEXHandler<Object,Object> MAHLER_PNUELI
		= fromGlobalSuffixFinder(GlobalSuffixFinders.MAHLER_PNUELI);
	
	public static ObservationTableCEXHandler<Object,Object> SHAHBAZ
		= fromGlobalSuffixFinder(GlobalSuffixFinders.SHAHBAZ);
	
	public static ObservationTableCEXHandler<Object,Object> FIND_LINEAR
		= fromLocalSuffixFinder(LocalSuffixFinders.FIND_LINEAR, false);
	
	public static ObservationTableCEXHandler<Object,Object> FIND_LINEAR_ALLSUFFIXES
		= fromLocalSuffixFinder(LocalSuffixFinders.FIND_LINEAR, true);
	
	public static ObservationTableCEXHandler<Object,Object> FIND_LINEAR_REVERSE
		= fromLocalSuffixFinder(LocalSuffixFinders.FIND_LINEAR_REVERSE, false);
	
	public static ObservationTableCEXHandler<Object,Object> FIND_LINEAR_REVERSE_ALLSUFFIXES
		= fromLocalSuffixFinder(LocalSuffixFinders.FIND_LINEAR_REVERSE, true);
	
	public static ObservationTableCEXHandler<Object,Object> RIVEST_SCHAPIRE
		= fromLocalSuffixFinder(LocalSuffixFinders.RIVEST_SCHAPIRE, false);
	
	public static ObservationTableCEXHandler<Object,Object> RIVEST_SCHAPIRE_ALLSUFFIXES
		= fromLocalSuffixFinder(LocalSuffixFinders.RIVEST_SCHAPIRE, true);
		
	
	
	public static <I,O> 
	ObservationTableCEXHandler<I, O> fromGlobalSuffixFinder(final GlobalSuffixFinder<I,O> globalFinder) {
		return new ObservationTableCEXHandler<I, O>() {
			@Override
			public <RI extends I,RO extends O>
			List<List<Row<RI>>> handleCounterexample(
					DefaultQuery<RI,RO> ceQuery, ObservationTable<RI,RO> table,
					SuffixOutput<RI,RO> hypOutput, MembershipOracle<RI,RO> oracle) {
				List<Word<RI>> suffixes = globalFinder.findSuffixes(ceQuery, table, hypOutput, oracle);
				return handleGlobalSuffixes(table, suffixes, oracle);
			}
			@Override
			public boolean needsConsistencyCheck() {
				return false;
			}
		};
	}
	
	public static <I,O>
	ObservationTableCEXHandler<I, O> fromLocalSuffixFinder(final LocalSuffixFinder<I,O> localFinder, final boolean allSuffixes) {
		return new ObservationTableCEXHandler<I, O>() {
			@Override
			public <RI extends I,RO extends O>
			List<List<Row<RI>>> handleCounterexample(
					DefaultQuery<RI,RO> ceQuery, ObservationTable<RI,RO> table,
					SuffixOutput<RI,RO> hypOutput, MembershipOracle<RI,RO> oracle) {
				int suffixIdx = localFinder.findSuffixIndex(ceQuery, table, hypOutput, oracle);
				return handleLocalSuffix(ceQuery, table, suffixIdx, allSuffixes, oracle);
			}
			@Override
			public boolean needsConsistencyCheck() {
				return false;
			}
		};
	}
	
	
	
	public static <I,O>
	ObservationTableCEXHandler<I, O> fromLocalSuffixFinder(final LocalSuffixFinder<I,O> localFinder) {
		return fromLocalSuffixFinder(localFinder, false);
	}
	
	public static <I,O>
	List<List<Row<I>>> handleClassicLStar(DefaultQuery<I, O> ceQuery,
			ObservationTable<I, O> table, MembershipOracle<I, O> oracle) {
		List<List<Row<I>>> unclosed = Collections.emptyList();
		
		List<Word<I>> suffixes = table.getSuffixes();
		
		Word<I> ceWord = ceQuery.getInput();
		int ceLen = ceWord.length();
		
		for(int i = 1; i <= ceLen; i++) {
			Word<I> suffix = ceWord.suffix(i);
			if(suffixes != null) {
				if(suffixes.contains(suffix))
					continue;
				suffixes = null;
			}
			
			unclosed = table.addSuffix(suffix, oracle);
			if(!unclosed.isEmpty())
				break;
		}
		
		return unclosed;
	}
	
	public static <I,O>
	List<List<Row<I>>> handleSuffix1by1(DefaultQuery<I, O> ceQuery,
			ObservationTable<I, O> table, MembershipOracle<I, O> oracle) {
		List<List<Row<I>>> unclosed = Collections.emptyList();
		
		List<Word<I>> suffixes = table.getSuffixes();
		
		Word<I> ceWord = ceQuery.getInput();
		int ceLen = ceWord.length();
		
		for(int i = 1; i <= ceLen; i++) {
			Word<I> suffix = ceWord.suffix(i);
			if(suffixes != null) {
				if(suffixes.contains(suffix))
					continue;
				suffixes = null;
			}
			
			unclosed = table.addSuffix(suffix, oracle);
			if(!unclosed.isEmpty())
				break;
		}
		
		return unclosed;
	}
	
	public static <I,O>
	List<List<Row<I>>> handleGlobalSuffixes(ObservationTable<I,O> table, List<Word<I>> suffixes,
			MembershipOracle<I,O> oracle) {
		return table.addSuffixes(suffixes, oracle);
	}
	
	public static <I,O>
	List<List<Row<I>>> handleLocalSuffix(Query<I,O> ceQuery, ObservationTable<I,O> table,
			int suffixIndex, boolean allSuffixes, MembershipOracle<I,O> oracle) {
		List<Word<I>> suffixes
			= GlobalSuffixFinders.suffixesForLocalOutput(ceQuery, suffixIndex, allSuffixes);
		return handleGlobalSuffixes(table, suffixes, oracle);
	}
	
	public static <I,O>
	List<List<Row<I>>> handleLocalSuffix(Query<I,O> ceQuery, ObservationTable<I,O> table,
			int suffixIndex, MembershipOracle<I,O> oracle) {
		return handleLocalSuffix(ceQuery, table, suffixIndex, false, oracle);
	}
}

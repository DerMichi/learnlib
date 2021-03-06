package de.learnlib.mealy;

import java.util.Objects;

import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.words.Word;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.oracles.DefaultQuery;


/**
 * Utility class helping to unify various approaches to actively learning
 * Mealy machines.
 *  
 * @author Malte Isberner <malte.isberner@gmail.com>
 *
 */
public abstract class MealyUtil {
	
	public static final int NO_MISMATCH = -1;
	
	public static <O> int findMismatch(Word<O> out1, Word<O> out2) {
		int len = out1.length();
		assert len == out2.length();
		
		for(int i = 0; i < len; i++) {
			O sym1 = out1.getSymbol(i);
			O sym2 = out2.getSymbol(i);
			
			if(!Objects.equals(sym1, sym2))
				return i;
		}
		
		return NO_MISMATCH;
	}
	
	
	public static <I,O> DefaultQuery<I,Word<O>> shortenCounterExample(
			MealyMachine<?,I,?,O> hypothesis,
			DefaultQuery<I,Word<O>> ceQuery) {
		Word<I> cePrefix = ceQuery.getPrefix(), ceSuffix = ceQuery.getSuffix();
		Word<O> hypOut = hypothesis.computeSuffixOutput(cePrefix, ceSuffix);
		Word<O> ceOut = ceQuery.getOutput();
		assert ceOut.length() == hypOut.length();
		
		int mismatchIdx = findMismatch(hypOut, ceOut);
		if(mismatchIdx == NO_MISMATCH)
			return null;
		
		return new DefaultQuery<>(cePrefix,
				ceSuffix.prefix(mismatchIdx + 1),
				ceOut.prefix(mismatchIdx + 1));
	}
	
	public static <I,O> DefaultQuery<I,O> reduceCounterExample(
			MealyMachine<?,I,?,O> hypothesis,
			DefaultQuery<I,Word<O>> ceQuery) {
		Word<I> cePrefix = ceQuery.getPrefix(), ceSuffix = ceQuery.getSuffix();
		Word<O> hypOut = hypothesis.computeSuffixOutput(cePrefix, ceSuffix);
		Word<O> ceOut = ceQuery.getOutput();
		assert ceOut.length() == hypOut.length();
		
		int mismatchIdx = findMismatch(hypOut, ceOut);
		if(mismatchIdx == NO_MISMATCH)
			return null;
		
		return new DefaultQuery<>(cePrefix,
				ceSuffix.prefix(mismatchIdx + 1),
				ceOut.getSymbol(mismatchIdx));
	}
	
	
	public static <M extends MealyMachine<?,I,?,O>,I,O>
	LearningAlgorithm<M,I,Word<O>> wrapSymbolLearner(LearningAlgorithm<M,I,O> learner) {
		return new MealyLearnerWrapper<>(learner);
	}
	
	public static <I,O> MembershipOracle<I, O> wrapWordOracle(MembershipOracle<I,Word<O>> oracle) {
		return new SymbolOracleWrapper<>(oracle);
	}
	
	// Prevent inheritance
	private MealyUtil() {}

}

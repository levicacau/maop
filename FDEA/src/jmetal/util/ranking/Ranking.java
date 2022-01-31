package jmetal.util.ranking;

import jmetal.core.SolutionSet;

import java.util.List;

public interface Ranking {
	public SolutionSet getSubfront(int layer);
	public int getNumberOfSubfronts();
}
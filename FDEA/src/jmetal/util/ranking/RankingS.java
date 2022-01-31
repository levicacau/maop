package jmetal.util.ranking;

import java.util.List;

public interface RankingS<S> {
    RankingS<S> compute(List<S> solutionList) ;
    List<S> getSubFront(int rank) ;
    int getNumberOfSubFronts() ;
    Integer getRank(S solution) ;
    Object getAttributedId() ;
}
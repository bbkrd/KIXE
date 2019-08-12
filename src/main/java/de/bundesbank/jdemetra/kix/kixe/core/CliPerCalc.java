/*
 * Copyright 2016 Deutsche Bundesbank
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package de.bundesbank.jdemetra.kix.kixe.core;

import static de.bundesbank.jdemetra.kix.kixe.core.LastPeriodOverlapMethods.chain;
import static de.bundesbank.jdemetra.kix.kixe.core.LastPeriodOverlapMethods.transform;
import static de.bundesbank.jdemetra.kix.kixe.core.LastPeriodOverlapMethods.unchain;
import de.bundesbank.kix.parser.ICalc;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;

/**
 *
 * @author Thomas Witthohn
 */
public class CliPerCalc implements ICalc {

    private double factor, factorWeight;
    private final boolean displayFirstYear;
    private final int referenzYear;
    private TsData weightedSumData;
    private TsData weightedSumWeights;

    public CliPerCalc(TsData indexData, TsData indexWeights, int referenzYear, boolean displayFirstYear) {
        this.displayFirstYear = displayFirstYear;
        this.referenzYear = referenzYear;

        this.weightedSumData = unchain(indexData);
        this.weightedSumWeights = indexWeights;

        factorWeight = weightInRefYear(indexData, indexWeights);
        factor = meanInRefYear(indexData);

    }

    @Override
    public void plus(TsData index, TsData weight) {
        addToWeightSum(unchain(index), weight);
        factor = addToFactor(factor, factorWeight, index, weightInRefYear(index, weight), referenzYear);
        factorWeight += weightInRefYear(index, weight);

    }

    @Override
    public void minus(TsData index, TsData weight) {
        subtractFromWeightSum(unchain(index), weight);
        factor = subtractFromFactor(factor, factorWeight, index, weightInRefYear(index, weight), referenzYear);
        factorWeight -= weightInRefYear(index, weight);
    }

    @Override
    public TsData getResult() {

        TsData indexData = scaleToRefYear(chain(weightedSumData), factor);
        if (displayFirstYear) {
            return indexData;
        }
        return indexData.drop(indexData.getFrequency().intValue() - indexData.getStart().getPosition(), 0);

    }

    /**
     *
     * @param factor
     * @param weightFactor
     * @param index
     * @param weightIndex_InRefYear
     * @param refYear
     *
     * @return
     */
    private double subtractFromFactor(double factor, double weightFactor, TsData index, double weightIndex_InRefYear, int refYear) {
        return (factor * weightFactor - meanInRefYear(index) * weightIndex_InRefYear) / (weightFactor - weightIndex_InRefYear);
    }

    /**
     *
     * @param factor
     * @param weightFactor
     * @param index
     * @param weightIndex_InRefYear
     * @param refYear
     *
     * @return
     */
    private double addToFactor(double factor, double weightFactor, TsData index, double weightIndex_InRefYear, int refYear) {
        return (factor * weightFactor + meanInRefYear(index) * weightIndex_InRefYear) / (weightFactor + weightIndex_InRefYear);
    }

    /**
     *
     * @param index
     * @param weight
     *
     */
    private void addToWeightSum(final TsData index, final TsData weight) {
        TsData transformedWeightedSumWeights = transform(weightedSumWeights);
        TsData transformedWeight = transform(weight);

        weightedSumData = weightedSumData.times(transformedWeightedSumWeights).plus(index.times(transformedWeight)).div(transformedWeightedSumWeights.plus(transformedWeight));
        weightedSumWeights = weightedSumWeights.plus(weight);
    }

    /**
     *
     * @param index
     * @param weight
     *
     */
    private void subtractFromWeightSum(final TsData index, final TsData weight) {
        TsData transformedWeightedSumWeights = transform(weightedSumWeights);
        TsData transformedWeight = transform(weight);

        weightedSumData = weightedSumData.times(transformedWeightedSumWeights).minus(index.times(transformedWeight)).div(transformedWeightedSumWeights.minus(transformedWeight));
        weightedSumWeights = weightedSumWeights.minus(weight);
    }

    /**
     *
     * @param index
     * @param weight
     * @param refYear
     *
     * @return
     */
    private double weightInRefYear(TsData index, TsData weight) {
        TsFrequency frequency = index.getFrequency();
        double meanInRefYear = meanInRefYear(index);
        double lastValuePreviousYearIndex = index.get(new TsPeriod(frequency, referenzYear - 1, frequency.intValue() - 1));
        double lastValuePreviousYearWeight = weight.get(new TsPeriod(frequency, referenzYear - 1, frequency.intValue() - 1));
        return meanInRefYear / lastValuePreviousYearIndex * lastValuePreviousYearWeight;
    }

    /**
     *
     * @param chainedTs
     * @param factor
     *
     * @return
     */
    private TsData scaleToRefYear(TsData chainedTs, double factor) {
        return chainedTs.times(factor).div(meanInRefYear(chainedTs));
    }

    /**
     *
     * @param data
     * @param refYear
     *
     * @return
     */
    private double meanInRefYear(TsData data) {

        double sum = 0;
        TsFrequency frequency = data.getFrequency();
        for (int i = 0; i < frequency.intValue(); i++) {
            sum += data.get(new TsPeriod(frequency, referenzYear, i));
        }
        return sum / frequency.intValue();
    }
}

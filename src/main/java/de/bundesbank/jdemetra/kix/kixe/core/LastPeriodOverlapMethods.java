/*
 * Copyright 2017 Deutsche Bundesbank
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

import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsDomain;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;

/**
 *
 * @author Thomas Witthohn
 */
public final class LastPeriodOverlapMethods {

    /**
     *
     * @param weightSum
     *
     * @return
     */
    public static TsData chain(TsData weightSum) {
        TsData retVal = weightSum.clone();
        double chainingFactor = 100;
        int startYear = retVal.getStart().getYear();
        int endYear = retVal.getLastPeriod().getYear();
        TsFrequency frequency = retVal.getFrequency();

        for (int i = 1; i <= endYear - startYear; ++i) {
            chainingFactor *= weightSum.get(new TsPeriod(frequency, startYear + i - 1, frequency.intValue() - 1)) / 100;
            if (i == endYear - startYear) {
                for (int k = 0; k <= retVal.getLastPeriod().getPosition(); ++k) {
                    double chainedValue = retVal.get(new TsPeriod(frequency, startYear + i, k)) * chainingFactor / 100;
                    retVal.set(new TsPeriod(frequency, startYear + i, k), chainedValue);
                }
            } else {
                for (int k = 0; k < frequency.intValue(); ++k) {
                    double chainedValue = retVal.get(new TsPeriod(frequency, startYear + i, k)) * chainingFactor / 100;
                    retVal.set(new TsPeriod(frequency, startYear + i, k), chainedValue);
                }
            }
        }
        return retVal;
    }

    /**
     * Each value in the time series is divided by the respective previous year
     * final value (month or quarter) then multiplied by 100.
     *
     * @param index
     *
     * @return new TsData
     */
    public static TsData unchain(TsData index) {
        return index.times(100).div(transform(index));

    }

    //TODO: ordentlicher Name
    /**
     *
     * @param index
     *
     * @return
     */
    public static TsData transform(TsData index) {
        int startYear = index.getStart().getYear();
        int startPosition = index.getStart().getPosition();
        int endYear = index.getLastPeriod().getYear();
        TsFrequency frequency = index.getFrequency();

        int count = frequency.intValue() * (index.getEnd().getYear() - startYear) + (frequency.intValue() - startPosition);
        TsData retVal = new TsData(new TsDomain(frequency, startYear, startPosition, count));

        //First year
        {
            double lastValueThisYear = index.get(new TsPeriod(frequency, startYear, frequency.intValue() - 1));
            for (int i = startPosition; i < frequency.intValue(); ++i) {
                retVal.set(new TsPeriod(frequency, startYear, i), lastValueThisYear);
            }
        }

        //all other years
        for (int i = 1; i <= endYear - startYear; ++i) {
            double lastValuePreviousYear = index.get(new TsPeriod(frequency, startYear + i - 1, frequency.intValue() - 1));

            for (int k = 0; k < frequency.intValue(); ++k) {
                retVal.set(new TsPeriod(frequency, startYear + i, k), lastValuePreviousYear);

            }
        }
        return retVal;
    }

    private LastPeriodOverlapMethods() {
    }

}

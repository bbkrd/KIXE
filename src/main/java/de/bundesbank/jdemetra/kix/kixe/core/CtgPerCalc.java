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
import ec.tstoolkit.timeseries.simplets.TsObservation;
import ec.tstoolkit.timeseries.simplets.TsPeriod;

/**
 *
 * @author Thomas Witthohn
 */
public final class CtgPerCalc {

    private CtgPerCalc() {

    }

    public static TsData contributionToGrowth(TsData unchainedContributor, TsData weightsContributor, TsData unchainedTotal, TsData weightsTotal, int lag) {
        if (lag < 0) {
            lag = unchainedContributor.getFrequency().intValue() * lag * -1;
        }
        TsDomain domain = unchainedContributor.lead(lag).getDomain().intersection(unchainedContributor.getDomain());
        TsData returnValue = new TsData(domain, 0);
        TsFrequency frequency = returnValue.getFrequency();

        for (TsObservation tsObservation : returnValue) {
            double value;
            TsPeriod period = tsObservation.getPeriod();
            int year = period.getYear();
            TsPeriod lastPeriodOneYearAgo = new TsPeriod(frequency, year - 1, frequency.intValue() - 1);
            TsPeriod lastPeriodTwoYearsAgo = new TsPeriod(frequency, year - 2, frequency.intValue() - 1);

            if (period.getPosition() < lag) {
                TsPeriod laggedPeriod = new TsPeriod(frequency, year - 1, period.getPosition() + frequency.intValue() - lag);
                value = ((weightsContributor.get(lastPeriodOneYearAgo) / weightsTotal.get(lastPeriodOneYearAgo))
                        * (unchainedTotal.get(lastPeriodOneYearAgo) / unchainedTotal.get(laggedPeriod))
                        * (unchainedContributor.get(period) - 100))
                        + ((weightsContributor.get(lastPeriodTwoYearsAgo) / weightsTotal.get(lastPeriodTwoYearsAgo))
                        * (100 / unchainedTotal.get(laggedPeriod))
                        * (unchainedContributor.get(lastPeriodOneYearAgo) - unchainedContributor.get(laggedPeriod)));
            } else {
                TsPeriod laggedPeriod = new TsPeriod(frequency, year, period.getPosition() - lag);
                value = (weightsContributor.get(lastPeriodOneYearAgo) / weightsTotal.get(lastPeriodOneYearAgo))
                        * (100 / unchainedTotal.get(laggedPeriod))
                        * (unchainedContributor.get(period) - unchainedContributor.get(laggedPeriod));
            }
            returnValue.set(period, value);
        }

        return returnValue.cleanExtremities();
    }
}

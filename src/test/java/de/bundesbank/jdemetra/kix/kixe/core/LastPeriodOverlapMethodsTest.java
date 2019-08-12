/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.kixe.core;

import de.bundesbank.jdemetra.kix.kixe.TsDataAsserter;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import net.trajano.commons.testing.UtilityClassTestUtil;
import org.junit.Test;

/**
 *
 * @author s4504tw
 */
public class LastPeriodOverlapMethodsTest {

    TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 2007, 3);

    double[] aData = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8,
        92.4, 95.6, 98.8, 103.5, 102.0, 107.4, 110.6, 113.9,
        117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7,
        149.0};
    TsData a = new TsData(start, aData, false);

    public LastPeriodOverlapMethodsTest() {
    }

    @Test
    public void testTransform() {
        double[] expResultData = {75.1, 75.1, 75.1, 75.1, 75.1, 81.3, 81.3, 81.3, 81.3,
            92.4, 92.4, 92.4, 92.4, 102.0, 102.0, 102.0, 102.0, 117.5, 117.5, 117.5, 117.5,
            132.4, 132.4, 132.4, 132.4, Double.NaN, Double.NaN, Double.NaN, Double.NaN};
        TsData expResult = new TsData(start, expResultData, false);
        TsData result = LastPeriodOverlapMethods.transform(a);
        TsDataAsserter.assertTsDataEquals(expResult, result, 1E-13);
    }

    @Test
    public void testChainSum_TsData() {
        double[] weightedSumData = {100, 102.78260, 104.55982, 103.87845, 105.02323,
            103.49592, 105.57702, 106.40975, 107.24248,
            101.04537, 102.14469, 102.66667, 101.30152,
            102.69590, 103.76980, 104.78352, 105.83097,
            100.77294, 101.38873, 101.97067, 102.62435,
            100.43248, 100.77719, 100.97571, 101.82925};

        TsData weightedSum = new TsData(new TsPeriod(TsFrequency.Quarterly, 2007, 3), weightedSumData, false);

        double[] expResultData = {100, 102.78260, 104.55982, 103.87845,
            105.02323, 108.69476, 110.88040, 111.75495,
            112.62951, 113.80690, 115.04507, 115.63297,
            114.09541, 117.17132, 118.39658, 119.55319,
            120.74828, 121.68159, 122.42515, 123.12783,
            123.91713, 124.45306, 124.88021, 125.12620,
            126.18389};
        TsData expResult = new TsData(new TsPeriod(TsFrequency.Quarterly, 2007, 3), expResultData, false);

        TsData result = LastPeriodOverlapMethods.chain(weightedSum);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);
    }

    @Test
    public void testChainSum_TsDataNaN() {
        double[] weightedSumData = {Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            95.9455678781636, 101.5235478245180, 99.1155647893106, 103.4153195080070,
            102.4501592599300, 105.9303326930090, 109.0691592281930, 114.3861870989490,
            106.8300670944030, 110.3457312146300, 112.8987184226080, 124.0662022472970,
            104.9318938491780, 106.4883823302890, 108.0821970617710, 113.5583078139040,
            103.9447394183710, 103.3979307247260, 102.1310926275610, 99.0086432985022,
            84.6808550674616, 83.7602791011846, 86.3681995465272, 90.7496460061146};

        TsData weightedSum = new TsData(new TsPeriod(TsFrequency.Quarterly, 2007, 3), weightedSumData, true);

        double[] expResultData = {Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN};

        TsData expResult = new TsData(new TsPeriod(TsFrequency.Quarterly, 2007, 3), expResultData, false);

        TsData result = LastPeriodOverlapMethods.chain(weightedSum);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);
    }

    @Test
    public void testUnchain_TsData() {
        TsData expResult = new TsData(TsFrequency.Quarterly, 2007, 3, new double[]{100, 103.99467,
            106.39148, 106.12517, 108.25566, 105.90406,
            109.71710, 111.68512, 113.65314, 103.46320,
            106.92641, 112.01299, 110.38961, 105.29412,
            108.43137, 111.66667, 115.19608, 103.06383,
            106.21277, 109.27660, 112.68085, 102.87009,
            106.19335, 109.29003, 112.53776}, false);
        TsData result = LastPeriodOverlapMethods.unchain(a);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);
    }

    @Test
    public void TestUnchainQuarterlyFirstQuarter() {
        double[] data = {78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
            107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2008, 0, data, false);

        TsData result = LastPeriodOverlapMethods.unchain(a);

        double[] expResultData = {96.064, 98.278, 98.032, 100, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320, 106.92641, 112.01299, 110.38961, 105.29412, 108.43137,
            111.66667, 115.19608, 103.06383, 106.21277, 109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2008, 0, expResultData, false);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);
    }

    @Test
    public void testLastPeriodOverlapMethodsWellDefined() throws ReflectiveOperationException {
        UtilityClassTestUtil.assertUtilityClassWellDefined(LastPeriodOverlapMethods.class);
    }

}

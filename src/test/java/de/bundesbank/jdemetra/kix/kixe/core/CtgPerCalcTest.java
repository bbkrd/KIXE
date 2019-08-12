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
public class CtgPerCalcTest {

    public CtgPerCalcTest() {
    }

    @Test
    public void TestWBGE_ContributionToGrowth_QuarterlyLagOne() {
        double[] unchainedA = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320,
            106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608, 103.06383, 106.21277,
            109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData uA = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedA, true);

        double[] weightA = {455.0, 455.0, 455.0, 455.0, 408.0, 408.0, 408.0, 408.0, 290.0, 290.0, 290.0, 290.0, 395.6, 395.6,
            395.6, 395.6, 368.5, 368.5, 368.5, 368.5, 290.9, 290.9, 290.9, 290.9};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weightA, true);

        double[] unchainedTotal = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57703, 106.40975, 107.24248, 101.04537,
            102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78351, 105.83097, 100.77294, 101.38873,
            101.97067, 102.62435, 100.43249, 100.77719, 100.97570, 101.82925};
        TsData uTotal = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedTotal, true);

        double[] weightTotal = {631.0, 631.0, 631.0, 631.0, 603.0, 603.0, 603.0, 603.0, 656.0, 656.0, 656.0, 656.0, 826.1,
            826.1, 826.1, 826.1, 926.5, 926.5, 926.5, 926.5, 939.5, 939.5, 939.5, 939.5};
        TsData wTotal = new TsData(TsFrequency.Quarterly, 2007, 3, weightTotal, true);

        int lag = 1;

        TsData result = CtgPerCalc.contributionToGrowth(uA, wA, uTotal, wTotal, lag);

        double[] expResultData = {1.681493, -0.183657, 1.478893, 3.994786, 2.492819, 1.261255, 1.251385, 1.530989, 1.515150,
            2.201427, -0.699011, 2.535229, 1.462919, 1.493023, 1.612995, 1.218587, 1.242831, 1.201896,
            1.327819, 0.888674, 1.024560, 0.951438, 0.995888};

        TsData expResult = new TsData(new TsPeriod(TsFrequency.Quarterly, 2008, 1), expResultData, false);

        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);

    }

    @Test
    public void TestWBGE_ContributionToGrowth_QuarterlyLagFour() {
        double[] unchainedA = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320,
            106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608, 103.06383, 106.21277,
            109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData uA = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedA, true);

        double[] weightA = {455.0, 455.0, 455.0, 455.0, 408.0, 408.0, 408.0, 408.0, 290.0, 290.0, 290.0, 290.0, 395.6, 395.6,
            395.6, 395.6, 368.5, 368.5, 368.5, 368.5, 290.9, 290.9, 290.9, 290.9};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weightA, true);

        double[] unchainedTotal = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57703, 106.40975, 107.24248, 101.04537,
            102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78351, 105.83097, 100.77294, 101.38873,
            101.97067, 102.62435, 100.43249, 100.77719, 100.97570, 101.82925};
        TsData uTotal = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedTotal, true);

        double[] weightTotal = {631.0, 631.0, 631.0, 631.0, 603.0, 603.0, 603.0, 603.0, 656.0, 656.0, 656.0, 656.0, 826.1,
            826.1, 826.1, 826.1, 926.5, 926.5, 926.5, 926.5, 939.5, 939.5, 939.5, 939.5};
        TsData wTotal = new TsData(TsFrequency.Quarterly, 2007, 3, weightTotal, true);

        int lag = 4;

        TsData result = CtgPerCalc.contributionToGrowth(uA, wA, uTotal, wTotal, lag);

        double[] expResultData = {7.071192, 7.889490, 9.472371, 9.237943, 6.652463, 5.632791, 6.603563, 4.592968, 5.571957, 5.503102,
            4.813595, 7.277047, 5.873127, 5.641882, 5.339490, 5.043598, 4.700673, 4.478372, 4.222756, 3.882103};

        TsData expResult = new TsData(new TsPeriod(TsFrequency.Quarterly, 2009, 0), expResultData, false);

        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);
    }

    @Test
    public void TestWBGE_ContributionToGrowth_QuarterlyLagMinusOne() {
        double[] unchainedA = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320,
            106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608, 103.06383, 106.21277,
            109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData uA = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedA, true);

        double[] weightA = {455.0, 455.0, 455.0, 455.0, 408.0, 408.0, 408.0, 408.0, 290.0, 290.0, 290.0, 290.0, 395.6, 395.6,
            395.6, 395.6, 368.5, 368.5, 368.5, 368.5, 290.9, 290.9, 290.9, 290.9};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weightA, true);

        double[] unchainedTotal = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57703, 106.40975, 107.24248, 101.04537,
            102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78351, 105.83097, 100.77294, 101.38873,
            101.97067, 102.62435, 100.43249, 100.77719, 100.97570, 101.82925};
        TsData uTotal = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedTotal, true);

        double[] weightTotal = {631.0, 631.0, 631.0, 631.0, 603.0, 603.0, 603.0, 603.0, 656.0, 656.0, 656.0, 656.0, 826.1,
            826.1, 826.1, 826.1, 926.5, 926.5, 926.5, 926.5, 939.5, 939.5, 939.5, 939.5};
        TsData wTotal = new TsData(TsFrequency.Quarterly, 2007, 3, weightTotal, true);

        int lag = -1;

        TsData result = CtgPerCalc.contributionToGrowth(uA, wA, uTotal, wTotal, lag);

        double[] expResultData = {7.071192, 7.889490, 9.472371, 9.237943, 6.652463, 5.632791, 6.603563, 4.592968, 5.571957, 5.503102,
            4.813595, 7.277047, 5.873127, 5.641882, 5.339490, 5.043598, 4.700673, 4.478372, 4.222756, 3.882103};

        TsData expResult = new TsData(new TsPeriod(TsFrequency.Quarterly, 2009, 0), expResultData, false);

        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);
    }

    @Test
    public void testCtgPerCalcMethodsWellDefined() throws ReflectiveOperationException {
        UtilityClassTestUtil.assertUtilityClassWellDefined(CtgPerCalc.class);
    }

}

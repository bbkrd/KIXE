/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.kixe;

import ec.tstoolkit.timeseries.regression.TsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.utilities.DefaultNameValidator;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Deutsche Bundesbank
 */
public class CtgPerTest {

    public CtgPerTest() {
    }

    private final String VALIDATOR = ",= +-";
    double[] i1Data = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8,
        92.4, 95.6, 98.8, 103.5, 102.0, 107.4, 110.6, 113.9,
        117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7,
        149.0};

    double[] w1Data = {455.0, 455.0, 455.0, 455.0, 408.0, 408.0, 408.0, 408.0,
        290.0, 290.0, 290.0, 290.0, 395.6, 395.6, 395.6, 395.6,
        368.5, 368.5, 368.5, 368.5, 290.9, 290.9, 290.9, 290.9};

    double[] i2Data = {87.21547, 89.64233, 91.19234, 90.59808, 91.59650,
        94.79864, 96.70486, 97.46761, 98.23036, 99.25723, 100.33710, 100.84984,
        99.50885, 102.19151, 103.26013, 104.26887, 105.31118, 106.12517,
        106.77367, 107.38652, 108.07491, 108.54232, 108.91486, 109.12940,
        110.05187};

    double[] w2Data = {631.0, 631.0, 631.0, 631.0, 603.0, 603.0, 603.0, 603.0,
        656.0, 656.0, 656.0, 656.0, 826.1, 826.1, 826.1, 826.1,
        926.5, 926.5, 926.5, 926.5, 939.5, 939.5, 939.5, 939.5};

    private final TsVariables indices = new TsVariables("i", new DefaultNameValidator(VALIDATOR));
    private final TsVariables weights = new TsVariables("w", new DefaultNameValidator(VALIDATOR));
    CtgPer instance = new CtgPer();

    private void quarterlyData() {
        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2007, 3, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2007, 3, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2007, 3, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2007, 3, w2Data, true)));
    }

    @Test
    public void testCompute_ctg() {
        String formula = "ctg.per,i1,w1,i2,w2,1";
        quarterlyData();
        double[] expResultData = {1.68149273309945, -0.18365690519025,
            1.47889252765230, 3.99478621651888,
            2.49281914498865, 1.26125490032722,
            1.25138472730026, 1.53098933586738,
            1.51515041895098, 2.20142673424931,
            -0.69901093307269, 2.53522931990857,
            1.46291926170490, 1.49302289879950,
            1.61299503300383, 1.21858745450161,
            1.24283079603342, 1.20189633611696,
            1.32781907580994, 0.88867415173913,
            1.02456003350763, 0.95143813243082,
            0.99588805285211};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2008, 1, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);
    }

    @Test
    public void testCompute_ctgMinusOneLag() {
        String formula = "ctg.per,i1,w1,i2,w2,-1";
        quarterlyData();
        double[] expResultData = {7.071192, 7.889490, 9.472371, 9.237943,
            6.652463, 5.632791, 6.603563, 4.592968,
            5.571957, 5.503102, 4.813595, 7.277047,
            5.873127, 5.641882, 5.339490, 5.043598,
            4.700673, 4.478372, 4.222756, 3.882103};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2009, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);
    }

    @Test
    public void testCompute_MissingData() {
        String formula = "WBGE,i1,w1,i2,w2,1";
        indices.clear();
        weights.clear();
        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        String expErrorMessage = "The following data is not available: i1 i2 w1 w2";
        String resultErrorMessage = instance.getErrorMessage();
        Assert.assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testCompute_DataNotMeetingAssumptions() {
        String formula = "ctg.per,i1,w1,i2,w2,1";

        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w2Data, true)));

        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        String expErrorMessage = "The index series i1 begins before the corresponding weight series w1.";
        String resultErrorMessage = instance.getErrorMessage();
        Assert.assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testCompute_DataNotMeetingAssumptions2() {
        String formula = "ctg.per,i1,w1,i2,w2,1";

        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w2Data, true)));

        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        String expErrorMessage = "The index series i2 begins before the corresponding weight series w2.";
        String resultErrorMessage = instance.getErrorMessage();
        Assert.assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testCompute_DataNotMeetingAssumptions3() {
        String formula = "ctg.per,i1,w1,i2,w2,1";

        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2005, 0, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2005, 0, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w2Data, true)));

        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        String expErrorMessage = "The contributing index series (i1) should not begin after the total index series (i2).";
        String resultErrorMessage = instance.getErrorMessage();
        Assert.assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testCompute_DataNotMeetingAssumptionsAll() {
        String formula = "ctg.per,i1,w1,i2,w2,1";

        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2005, 0, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w2Data, true)));

        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        String expErrorMessage = "The index series i1 begins before the corresponding weight series w1.\n"
                + "The contributing index series (i1) should not begin after the total index series (i2).\n"
                + "The index series i2 begins before the corresponding weight series w2.";
        String resultErrorMessage = instance.getErrorMessage();
        Assert.assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testCompute_WrongFormula() {
        System.out.println("Start Compute_WrongFormula");

        String formula = "UNC.ANN,w1,2005";

        TsData expResultTsData = null;
        TsData resultTsData = instance.compute(formula, indices, weights);

        String expResultErrorMessage = "Formula doesn't match required syntax";
        TsDataAsserter.assertTsDataEquals(expResultTsData, resultTsData, 0.005);
        assertEquals(expResultErrorMessage, instance.getErrorMessage());

        System.out.println("End Compute_WrongFormula");
    }

    @Test
    public void testGetValidControlCharacter() {
        String[] expResult = {"wbge", "ctg.per"};
        String[] result = instance.getValidControlCharacter();
        assertArrayEquals(expResult, result);
    }

}

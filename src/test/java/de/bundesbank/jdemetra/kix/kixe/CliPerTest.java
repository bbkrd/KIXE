/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.kixe;

import de.bundesbank.jdemetra.kix.kixe.options.PerOptionsPanelController;
import static de.bundesbank.jdemetra.kix.kixe.options.PerOptionsPanelController.PER_DEFAULT_METHOD;
import ec.tstoolkit.timeseries.regression.TsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.utilities.DefaultNameValidator;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openide.util.NbPreferences;

/**
 *
 * @author Deutsche Bundesbank
 */
public class CliPerTest {

    public CliPerTest() {
    }

    private final String VALIDATOR = ",= +-";
    private final double[] i1Data = {89.78, 93.71, 90.85, 95.20, 95.47, 98.29, 100.15, 104.76, 107.69, 109.16, 112.40, 122.26,
        119.73, 120.83, 122.45, 128.06, 128.39, 127.53, 125.12, 118.38, 102.32, 100.80, 105.21, 109.90};
    private final double[] w1Data = {175.09, 184.14, 178.95, 187.45, 188.09, 194.33, 197.97, 207.50, 214.56, 219.10, 225.54, 245.62,
        241.17, 244.79, 247.22, 257.58, 259.71, 260.26, 256.27, 240.33, 204.06, 199.94, 208.43, 218.29};
    private final double[] i2Data = {81.19, 93.96, 95.71, 97.31, 89.17, 94.84, 104.22, 111.00, 99.15, 115.17, 113.44, 132.40, 112.77,
        118.62, 121.56, 131.39, 121.06, 121.56, 125.29, 139.41, 128.07, 129.15, 125.46, 135.75};
    private final double[] w2Data = {25.15, 29.51, 30.30, 30.85, 28.51, 30.68, 32.95, 36.13, 31.86, 37.94, 37.00, 42.43, 36.29,
        38.60, 40.18, 43.60, 40.62, 40.57, 42.46, 46.63, 40.28, 39.44, 39.73, 44.00};
    private final TsVariables indices = new TsVariables("i", new DefaultNameValidator(VALIDATOR));
    private final TsVariables weights = new TsVariables("w", new DefaultNameValidator(VALIDATOR));
    CliPer instance = new CliPer();

    private void quarterlyData() {
        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w2Data, true)));
    }

    @Test
    public void testCompute_Add2TsPragmatic() {
        String formula = "KIXE,i1,w1,+,i2,w2,2005";
        showFirstYear(true);
        quarterlyData();
        double[] expResultData = {88.58876, 93.74469, 91.52401, 95.49264,
            94.59634, 97.81158, 100.71445, 105.62539,
            106.46924, 109.99229, 112.53047, 123.67321,
            118.79002, 120.54374, 122.34560, 128.53707,
            127.35740, 126.68997, 125.14942, 121.36334,
            105.96989, 104.81759, 108.08206, 113.56458};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.0005);
    }

    @Test
    public void testCompute_Add2TsPuristic() {
        String formula = "KIXE,i1,w1,+,i2,w2,2005";
        showFirstYear(false);
        quarterlyData();
        double[] expResultData = {94.59634, 97.81158, 100.71445, 105.62539,
            106.46924, 109.99229, 112.53047, 123.67321,
            118.79002, 120.54374, 122.34560, 128.53707,
            127.35740, 126.68997, 125.14942, 121.36334,
            105.96989, 104.81759, 108.08206, 113.56458};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2005, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.0005);
    }

    @Test
    public void testCompute_Subtract2TsPragmatic() {
        String formula = "KIXE,i1,w1,-,i2,w2,2005";
        showFirstYear(true);
        quarterlyData();
        double[] expResultData = {91.42844, 93.66198, 89.91729, 94.79503,
            96.67897, 98.95204, 99.36889, 103.56245,
            109.40057, 108.00895, 112.22749, 120.30030,
            121.01552, 121.21395, 122.58303, 127.39070,
            129.82661, 128.69801, 125.07524, 114.21484,
            97.22630, 95.19374, 101.20062, 104.78546};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.0005);
    }

    @Test
    public void testCompute_Subtract2TsPuristic() {
        String formula = "KIXE,i1,w1,-,i2,w2,2005";
        showFirstYear(false);
        quarterlyData();
        double[] expResultData = {96.67897, 98.95204, 99.36889, 103.56245,
            109.40057, 108.00895, 112.22749, 120.30030,
            121.01552, 121.21395, 122.58303, 127.39070,
            129.82661, 128.69801, 125.07524, 114.21484,
            97.22630, 95.19374, 101.20062, 104.78546};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2005, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.0005);
    }

    @Test
    public void testCompute_NullTestPragmatic() {
        String formula = "KIXE,i1,w1,+,i2,w2,-,i2,w2,2005";
        showFirstYear(true);
        quarterlyData();
        double[] expResultData = {89.78, 93.71, 90.85, 95.20, 95.47, 98.29, 100.15, 104.76, 107.69, 109.16, 112.40, 122.26,
            119.73, 120.83, 122.45, 128.06, 128.39, 127.53, 125.12, 118.38, 102.32, 100.80, 105.21, 109.90};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);
    }

    @Test
    public void testCompute_NullTestPuristic() {
        String formula = "KIXE,i1,w1,+,i2,w2,-,i2,w2,2005";
        showFirstYear(false);
        quarterlyData();
        double[] expResultData = {95.47, 98.29, 100.15, 104.76, 107.69, 109.16, 112.40, 122.26,
            119.73, 120.83, 122.45, 128.06, 128.39, 127.53, 125.12, 118.38, 102.32, 100.80, 105.21, 109.90};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2005, 0, expResultData, false);
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);
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
    public void testCompute_MissingData() {
        String formula = "KIXE,i1,w1,+,i2,w2,2005";
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
        String formula = "KIXE,i1,w1,+,i2,w2,2005";

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
        String formula = "KIXE,i1,w1,+,i2,w2,2005";

        indices.clear();
        weights.clear();
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, i1Data, true)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2003, 0, i2Data, true)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w1Data, true)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, w2Data, true)));

        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.005);

        String expErrorMessage = "The index series i1 begins before the corresponding weight series w1.\n"
                + "The index series i2 begins before the corresponding weight series w2.";
        String resultErrorMessage = instance.getErrorMessage();
        Assert.assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testGetValidControlCharacter() {
        String[] expResult = {"kixe", "cli.per"};
        String[] result = instance.getValidControlCharacter();
        assertArrayEquals(expResult, result);
    }

    private void showFirstYear(boolean showFirstYear) {
        NbPreferences.forModule(PerOptionsPanelController.class).putBoolean(PER_DEFAULT_METHOD, showFirstYear);

    }
}

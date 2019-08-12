package de.bundesbank.jdemetra.kix.kixe;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ec.tstoolkit.timeseries.regression.TsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.utilities.DefaultNameValidator;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Deutsche Bundesbank
 */
public class ChaPerTest {

    public ChaPerTest() {
    }
    private final String VALIDATOR = ",= +-";
    private final double[] i1Data = {102.78260, 104.55982, 103.87845, 105.02323,
        103.49592, 105.57702, 106.40975, 107.24248,
        101.04537, 102.14469, 102.66667, 101.30152,
        102.69590, 103.76980, 104.78352, 105.83097,
        100.77294, 101.38873, 101.97067, 102.62435,
        100.43248, 100.77719, 100.97571, 101.82925};
    private final TsVariables indices = new TsVariables("i", new DefaultNameValidator(VALIDATOR));
    private final TsVariables weights = new TsVariables("w", new DefaultNameValidator(VALIDATOR));

    @Before
    public void clearTsVariables() {
        indices.clear();
        weights.clear();
    }

    @Test
    public void testCompute_ChainIndex() {
        System.out.println("Start Compute_ChainIndex");
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i1Data, true)));

        String formula = "Cha.PER,i1";
        ChaPer instance = new ChaPer();
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, new double[]{102.78260, 104.55982, 103.87845,
            105.02323, 108.69476, 110.88040, 111.75495,
            112.62951, 113.80690, 115.04507, 115.63297,
            114.09541, 117.17132, 118.39658, 119.55319,
            120.74828, 121.68159, 122.42515, 123.12783,
            123.91713, 124.45306, 124.88021, 125.12620,
            126.18389},
                true);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);

        System.out.println("End Compute_ChainIndex");
    }

    @Test
    public void testCompute_ChainIndexWithRefYear() {
        System.out.println("Start Compute_ChainIndexWithRefYear");
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i1Data, true)));

        String formula = "Cha.PER,i1,2005";
        ChaPer instance = new ChaPer();
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, new double[]{92.60535902,
            94.20660374, 93.59270107, 94.6241282, 97.93211374,
            99.90133787, 100.6892924, 101.477256, 102.5380642,
            103.6536341, 104.1833219, 102.7980067, 105.5693489,
            106.6732871, 107.7153729, 108.7921284, 109.6330247,
            110.3029595, 110.9360622, 111.6472079, 112.1300716,
            112.5149265, 112.7365592, 113.6895198}, true);

        TsData result = instance.compute(formula, indices, weights);

        TsDataAsserter.assertTsDataEquals(expResult, result,
                0.00005);

        System.out.println(
                "End Compute_ChainIndexWithRefYear");
    }

    @Test
    public void testCompute_ChainWeight() {
        System.out.println("Start Compute_ChainWeight");
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i1Data, true)));

        String formula = "Cha.PER,w1";
        ChaPer instance = new ChaPer();
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, new double[]{102.78260, 104.55982, 103.87845,
            105.02323, 108.69476, 110.88040, 111.75495,
            112.62951, 113.80690, 115.04507, 115.63297,
            114.09541, 117.17132, 118.39658, 119.55319,
            120.74828, 121.68159, 122.42515, 123.12783,
            123.91713, 124.45306, 124.88021, 125.12620,
            126.18389},
                true);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);

        System.out.println("End Compute_ChainWeight");
    }

    @Test
    public void testCompute_ChainNotFound() {
        System.out.println("Start Compute_ChainNotFound");

        String formula = "CHA.PER,w1";
        ChaPer instance = new ChaPer();

        TsData expResultTsData = null;
        TsData resultTsData = instance.compute(formula, indices, weights);

        String expResultErrorMessage = "w1 doesn't exist";
        TsDataAsserter.assertTsDataEquals(expResultTsData, resultTsData, 0.00005);
        assertEquals(expResultErrorMessage, instance.getErrorMessage());

        System.out.println("End Compute_ChainNotFound");
    }

    @Test
    public void testGetValidControlCharacter() {
        System.out.println("getValidControlCharacter");
        ChaPer instance = new ChaPer();
        String[] expResult = {"cha.per"};
        String[] result = instance.getValidControlCharacter();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testCompute_ChainWrongFormula() {
        System.out.println("Start Compute_ChainWrongFormula");

        String formula = "UNC.ANN,w1,2005";
        ChaPer instance = new ChaPer();

        TsData expResultTsData = null;
        TsData resultTsData = instance.compute(formula, indices, weights);

        String expResultErrorMessage = "Formula doesn't match required syntax";
        TsDataAsserter.assertTsDataEquals(expResultTsData, resultTsData, 0.00005);
        assertEquals(expResultErrorMessage, instance.getErrorMessage());

        System.out.println("End Compute_ChainWrongFormula");
    }

    @Test
    public void testCompute_ChainWhitespacesInFormula() {
        System.out.println("Start Compute_ChainWhitespacesInFormula");

        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2004, 0, i1Data, true)));

        String formula = "Cha.PER,   w1";
        ChaPer instance = new ChaPer();
        TsData expResult = new TsData(TsFrequency.Quarterly, 2004, 0, new double[]{102.78260, 104.55982, 103.87845,
            105.02323, 108.69476, 110.88040, 111.75495,
            112.62951, 113.80690, 115.04507, 115.63297,
            114.09541, 117.17132, 118.39658, 119.55319,
            120.74828, 121.68159, 122.42515, 123.12783,
            123.91713, 124.45306, 124.88021, 125.12620,
            126.18389},
                true);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.00005);

        System.out.println("End Compute_ChainWhitespacesInFormula");
    }

}
